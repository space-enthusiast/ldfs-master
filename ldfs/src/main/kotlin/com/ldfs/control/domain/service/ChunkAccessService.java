package com.ldfs.control.domain.service;

import com.ldfs.control.domain.model.entity.ChunkEntity;
import com.ldfs.control.domain.model.entity.ChunkState;
import com.ldfs.control.domain.repository.ChunkEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChunkAccessService {

    private final ChunkEntityRepository chunkEntityRepository;

    @Autowired
    public ChunkAccessService(ChunkEntityRepository chunkEntityRepository) {
        this.chunkEntityRepository = chunkEntityRepository;
    }

    public List<ChunkEntity> getFileChunks(Long fileId) {
        return chunkEntityRepository.findAllByFileId(fileId);
    }
    public List<ChunkEntity> getSpecificChunkOfFile(Long fileId, Long chunkId) {
        return chunkEntityRepository.findAllByFileId(fileId);
    }


    public void deleteFile(Long fileId) {
        //TODO
        List<ChunkEntity> deleteList = chunkEntityRepository.findAllByFileId(fileId);
        if(deleteList == null || deleteList.isEmpty()) {
            throw new EntityNotFoundException("File with ID" + fileId + "is not found within DB");
        }
        deleteList.forEach(chunkEntity -> chunkEntity.setStateChunk(ChunkState.DELETING));

    }

}
