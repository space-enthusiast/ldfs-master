package com.ldfs.control.domain.service;

import com.ldfs.control.domain.model.entity.ChunkEntity;
import com.ldfs.control.domain.model.entity.ChunkServerEntity;
import com.ldfs.control.domain.model.entity.ServerState;
import com.ldfs.control.domain.repository.ChunkEntityRepository;
import com.ldfs.control.domain.repository.ChunkServerEntityRepository;
import com.ldfs.main.dto.response.CreateFileResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class ChunkServerAccessService {
    private final ChunkServerEntityRepository chunkServerEntityRepository;
    private final ChunkEntityRepository chunkEntityRepository;
    private final Long CHUNK_SIZE = 64L;


    public ChunkServerAccessService(ChunkServerEntityRepository chunkServerEntityRepository, ChunkEntityRepository chunkEntityRepository) {
        this.chunkServerEntityRepository = chunkServerEntityRepository;
        this.chunkEntityRepository = chunkEntityRepository;
    }

    public List<ChunkServerEntity> findServersWithChunkMetadata(UUID fileId, Long sequence) {
        Set<UUID> serverIdsWithChunk = new HashSet<>(
                chunkEntityRepository
                        .findAllByFileUUIDAndChunkOrder(fileId,sequence)
                        .stream()
                        .map(ChunkEntity::getChunkServerId)
                        .toList()
        );
        return chunkServerEntityRepository.findAllById(serverIdsWithChunk);
    }

    @Deprecated
    public List<ChunkServerEntity> findServersWithFile(UUID fileId) {
        Set<UUID> serverIdsWithChunk = new HashSet<>(
                chunkEntityRepository
                        .findAllByFileUUID(fileId)
                        .stream()
                        .map(ChunkEntity::getChunkServerId)
                        .toList()
        );
        return chunkServerEntityRepository.findAllById(serverIdsWithChunk);
    }

    public ChunkServerEntity findServerWithSpecificChunk(ChunkEntity chunk) {
        return chunkServerEntityRepository.findById(chunk.chunkServerId).orElseThrow();
    }

    public ChunkServerEntity findServerWithInetSocketAddr(InetSocketAddress addr) {
        return chunkServerEntityRepository.findByIp(addr.getAddress().getHostAddress());
    }

    @Transactional
    public List<CreateFileResponse> findServersWithStorageSpace(int numberOfChunks) {
        List<ChunkServerEntity> chunkServerEntities = new LinkedList<>();
        for (int i = 0; i < numberOfChunks; i++) {
            ChunkServerEntity chunkServerEntity = chunkServerEntityRepository.findByLargestRemainingStorageSize().getFirst();
            chunkServerEntities.add(chunkServerEntity);
            chunkServerEntity.setRemainingStorageSize(chunkServerEntity.getRemainingStorageSize() - CHUNK_SIZE);
            chunkServerEntityRepository.save(chunkServerEntity);
        }
        return IntStream.range(0, chunkServerEntities.size()).mapToObj(idx -> new CreateFileResponse((long) idx,
                chunkServerEntities.get(idx).getIp(),
                chunkServerEntities.get(idx).getPort()))
                .toList();
    }

    public ChunkServerEntity makeServerDiscoverable(String ip, String port, Long remainingStorageSize) {
        ChunkServerEntity chunkServerEntity = chunkServerEntityRepository.findByIpAndPort(ip,port);
        if(chunkServerEntity == null) {
            chunkServerEntity = new ChunkServerEntity();
            chunkServerEntity.setId(UUID.randomUUID());
        }
        chunkServerEntity.setIp(ip);
        chunkServerEntity.setPort(port);
        chunkServerEntity.setRemainingStorageSize(remainingStorageSize);
        chunkServerEntity.setServerState(ServerState.NORMAL);
        return chunkServerEntityRepository.save(chunkServerEntity);
    }

    public List<ChunkServerEntity> findAll() {
        return chunkServerEntityRepository.findAll();
    }

}
