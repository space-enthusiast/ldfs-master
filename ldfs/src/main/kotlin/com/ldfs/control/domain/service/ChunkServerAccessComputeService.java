package com.ldfs.control.domain.service;

import com.ldfs.control.domain.model.entity.ChunkServerEntity;
import com.ldfs.control.domain.repository.ChunkServerEntityRepository;
import jakarta.transaction.Transactional;
import kotlin.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChunkServerAccessComputeService {

    private final ChunkServerEntityRepository chunkServerEntityRepository;
    private final Long CHUNK_SIZE = 65546L;

    @Transactional
    public void getMaximumRemainingSpace(List<Triple<UUID,String,String>> chunkServerEntities) {
        ChunkServerEntity chunkServerEntity = chunkServerEntityRepository.findByLargestRemainingStorageSizeByLock().getFirst();
        chunkServerEntities.add(new Triple<>(chunkServerEntity.getId(), chunkServerEntity.getIp(), chunkServerEntity.getPort()));
        chunkServerEntity.setRemainingStorageSize(chunkServerEntity.getRemainingStorageSize() - CHUNK_SIZE);
        chunkServerEntityRepository.save(chunkServerEntity);
    }

    @Autowired
    public ChunkServerAccessComputeService(ChunkServerEntityRepository chunkServerEntityRepository) {
        this.chunkServerEntityRepository = chunkServerEntityRepository;
    }
}
