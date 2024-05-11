package com.ldfs.control.domain.service;

import com.ldfs.control.domain.model.entity.FileEntity;
import com.ldfs.control.domain.repository.FileEntityRepository;
import com.ldfs.main.dto.response.CreateFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FileAccessService {
    private final FileEntityRepository fileEntityRepository;
    private final ChunkServerAccessService chunkServerAccessService;
    private final Long CHUNKSIZE = 64L; //MB

    @Autowired
    public FileAccessService(FileEntityRepository fileEntityRepository, ChunkServerAccessService chunkServerAccessService) {
        this.fileEntityRepository = fileEntityRepository;
        this.chunkServerAccessService = chunkServerAccessService;
    }

    public void save(String name, UUID directoryId, UUID fileUUID) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(name);
        fileEntity.setFileUUID(fileUUID);
        fileEntity.setDirectoryId(directoryId);
        fileEntity.setStatus("CREATING");
        fileEntityRepository.save(fileEntity);
    }

    public List<CreateFileResponse> chunkify (Long fileSize) {
        Integer totalChunkNumber = (int) (fileSize/CHUNKSIZE) + 1;
         return chunkServerAccessService.findServersWithStorageSpace(totalChunkNumber);
    }
}
