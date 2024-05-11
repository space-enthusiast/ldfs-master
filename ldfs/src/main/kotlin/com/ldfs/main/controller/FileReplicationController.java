package com.ldfs.main.controller;

import com.ldfs.control.domain.model.entity.ChunkEntity;
import com.ldfs.control.domain.service.ChunkAccessService;
import com.ldfs.control.domain.service.ChunkServerAccessService;
import com.ldfs.control.domain.service.FileAccessService;
import com.ldfs.main.dto.request.ChunkDetailsRequest;
import com.ldfs.main.dto.request.ChunkReplicationRequest;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
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

    @PostMapping("/fileCreationComplete")
    @Transactional
    public ResponseEntity<List<ChunkEntity>> fileChunkStoreAndComplete(@RequestBody ChunkReplicationRequest chunkReplicationRequest) {
        UUID fileId = chunkReplicationRequest.getFileId();
        List<ChunkEntity> savedChunks = new LinkedList<>();
        for(ChunkDetailsRequest chunkDetailsRequest : chunkReplicationRequest.getFileChunks()) {
            UUID chunkServerId = chunkServerAccessService.getChunkServerUUID(chunkDetailsRequest.chunkServerIp, chunkDetailsRequest.chunkServerPort);
            ChunkEntity createdChunk = chunkAccessService.createFileChunk(chunkDetailsRequest.getChunkUUID(),
                    fileId,
                    chunkDetailsRequest.getChunkOrder(),
                    chunkServerId);
            savedChunks.add(createdChunk);
        }

        fileAccessService.completeSave(chunkReplicationRequest.fileId);

        return ResponseEntity.ok().body(savedChunks);
    }


}
