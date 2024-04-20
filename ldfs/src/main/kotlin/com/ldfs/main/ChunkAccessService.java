package com.ldfs.main;

import com.ldfs.control.domain.model.entity.Chunk;
import com.ldfs.control.domain.repository.ChunkEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChunkAccessService {

    private final ChunkEntityRepository chunkEntityRepository;

    @Autowired
    public ChunkAccessService(ChunkEntityRepository chunkEntityRepository) {
        this.chunkEntityRepository = chunkEntityRepository;
    }

    public List<Chunk> getFileChunks(Long fileId) {
        return chunkEntityRepository.findAllByFileId(fileId);
    }

    public void deleteFile(Long fileId) {
        List<Chunk> deleteList = chunkEntityRepository.deleteAllByFileId(fileId);
        if(deleteList == null || deleteList.isEmpty()) {
            throw new EntityNotFoundException("File with ID" + fileId + "is not found within DB");
        }
    }

}
