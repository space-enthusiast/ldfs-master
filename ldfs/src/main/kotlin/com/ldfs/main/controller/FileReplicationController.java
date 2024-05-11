package com.ldfs.main.controller;

import com.ldfs.control.domain.model.entity.ChunkEntity;
import com.ldfs.control.domain.service.ChunkAccessService;
import com.ldfs.control.domain.service.ChunkServerAccessService;
import com.ldfs.control.domain.service.FileAccessService;
import com.ldfs.main.dto.request.ChunkReplicationRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/files/replication")
public class FileReplicationController {
    private final ChunkAccessService chunkAccessService;
    private final ChunkServerAccessService chunkServerAccessService;
    private final FileAccessService fileAccessService;

    @Autowired
    public FileReplicationController(ChunkAccessService chunkAccessService, ChunkServerAccessService chunkServerAccessService, FileAccessService fileAccessService) {
        this.chunkAccessService = chunkAccessService;
        this.chunkServerAccessService = chunkServerAccessService;
        this.fileAccessService = fileAccessService;
    }

    @PostMapping("/fileChunkStore")
    @Transactional
    public ResponseEntity<ChunkEntity> fileChunkStore(@RequestBody ChunkReplicationRequest chunkReplicationRequest) {
        UUID chunkServerId = chunkServerAccessService.getChunkServerUUID(chunkReplicationRequest.chunkServerIp, chunkReplicationRequest.chunkServerPort);
        ChunkEntity createdChunk =  chunkAccessService.createFileChunk(chunkReplicationRequest.getChunkUUID(),
                                            chunkReplicationRequest.getFileId(),
                                            chunkReplicationRequest.getChunkOrder(),
                                            chunkServerId);

        return ResponseEntity.ok().body(createdChunk);
    }

    @PostMapping("/fileCreationComplete")
    @Transactional
    public void fileCreationComplete(UUID fileUUID) {
        fileAccessService.completeSave(fileUUID);
    }
}
