package com.ldfs.control.domain.service;

import com.ldfs.control.domain.model.entity.ChunkEntity;
import com.ldfs.control.domain.model.entity.ChunkServerEntity;
import com.ldfs.control.domain.model.entity.ServerState;
import com.ldfs.control.domain.repository.ChunkEntityRepository;
import com.ldfs.control.domain.repository.ChunkServerEntityRepository;
import com.ldfs.main.dto.response.CreateFileResponse;
import jakarta.transaction.Transactional;
import kotlin.Triple;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class ChunkServerAccessService {
    private final ChunkServerEntityRepository chunkServerEntityRepository;
    private final ChunkEntityRepository chunkEntityRepository;
    private final Long CHUNK_SIZE = 64L;
    private final ChunkServerAccessComputeService chunkServerAccessComputeService;


    public ChunkServerAccessService(ChunkServerEntityRepository chunkServerEntityRepository, ChunkEntityRepository chunkEntityRepository, ChunkServerAccessComputeService chunkServerAccessComputeService) {
        this.chunkServerEntityRepository = chunkServerEntityRepository;
        this.chunkEntityRepository = chunkEntityRepository;
        this.chunkServerAccessComputeService = chunkServerAccessComputeService;
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

    public List<CreateFileResponse> findServersWithStorageSpace(int numberOfChunks) {
        List<Triple<UUID,String,String>> chunkServerEntities = new LinkedList<>();
        for (int i = 0; i < numberOfChunks; i++) {
            chunkServerAccessComputeService.getMaximumRemainingSpace(chunkServerEntities);
        }
        return IntStream.range(0, chunkServerEntities.size()).mapToObj(idx -> new CreateFileResponse((long) idx,
                chunkServerEntities.get(idx).getSecond(),
                chunkServerEntities.get(idx).getThird()))
                .toList();
    }

    @Transactional
    public ChunkServerEntity makeServerDiscoverable(String ip, String port, Long remainingStorageSize) {
        ChunkServerEntity chunkServerEntity = chunkServerEntityRepository.findByIpAndPortWithLock(ip,port);
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

    public UUID getChunkServerUUID(String chunkServerIp, String chunkServerPort) {
        return chunkServerEntityRepository.findByIpAndPort(chunkServerIp, chunkServerPort).getId();
    }
}
