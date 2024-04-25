package com.ldfs.control.domain.service;

import com.ldfs.control.domain.model.entity.ChunkEntity;
import com.ldfs.control.domain.model.entity.ChunkServerEntity;
import com.ldfs.control.domain.repository.ChunkEntityRepository;
import com.ldfs.control.domain.repository.ChunkServerEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class ChunkServerAccessService {
    private final ChunkServerEntityRepository chunkServerEntityRepository;
    private final ChunkEntityRepository chunkEntityRepository;


    public ChunkServerAccessService(ChunkServerEntityRepository chunkServerEntityRepository, ChunkEntityRepository chunkEntityRepository) {
        this.chunkServerEntityRepository = chunkServerEntityRepository;
        this.chunkEntityRepository = chunkEntityRepository;
    }

    public List<ChunkServerEntity> findServersWithChunkMetadata(Long fileId, Long sequence) {
        Set<Long> serverIdsWithChunk = new HashSet<>(
                chunkEntityRepository
                        .findAllByFileIdAndSequence(fileId,sequence)
                        .stream()
                        .map(ChunkEntity::getChunkServerId)
                        .toList()
        );
        return chunkServerEntityRepository.findAllById(serverIdsWithChunk);
    }

    @Deprecated
    public List<ChunkServerEntity> findServersWithFile(Long fileId) {
        Set<Long> serverIdsWithChunk = new HashSet<>(
                chunkEntityRepository
                        .findAllByFileId(fileId)
                        .stream()
                        .map(ChunkEntity::getChunkServerId)
                        .toList()
        );
        return chunkServerEntityRepository.findAllById(serverIdsWithChunk);
    }

    public ChunkServerEntity findServerWithSpecificChunk(ChunkEntity chunk) {
        return chunkServerEntityRepository.findById(chunk.chunkServerId).orElseThrow();
    }
}
