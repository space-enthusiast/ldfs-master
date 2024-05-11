package com.ldfs.control.domain.service;

import com.ldfs.control.domain.model.entity.ChunkEntity;
import com.ldfs.control.domain.model.entity.ChunkState;
import com.ldfs.control.domain.repository.ChunkEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChunkAccessService {

    private final ChunkEntityRepository chunkEntityRepository;

    @Autowired
    public ChunkAccessService(ChunkEntityRepository chunkEntityRepository) {
        this.chunkEntityRepository = chunkEntityRepository;
    }

    public List<ChunkEntity> getFileChunks(UUID fileId) {
        return chunkEntityRepository.findAllByFileUUID(fileId);
    }
    public List<ChunkEntity> getSpecificChunkOfFile(UUID fileId, Long chunkId) {
        return chunkEntityRepository.findAllByFileUUID(fileId);
    }

    public void deleteFile(UUID fileId) {
        //TODO
        List<ChunkEntity> deleteList = chunkEntityRepository.findAllByFileUUID(fileId);
        if(deleteList == null || deleteList.isEmpty()) {
            throw new EntityNotFoundException("File with ID" + fileId + "is not found within DB");
        }
        deleteList.forEach(chunkEntity -> chunkEntity.setStateChunk(ChunkState.DELETING));
    }


}
