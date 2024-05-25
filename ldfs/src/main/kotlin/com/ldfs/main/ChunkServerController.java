package com.ldfs.main;

import com.ldfs.control.domain.model.entity.ChunkServerEntity;
import com.ldfs.control.domain.service.ChunkServerAccessService;
import com.ldfs.main.dto.request.HeartBeatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chunkServer")
public class ChunkServerController {
    private final ChunkServerAccessService chunkServerAccessService;

    @Autowired
    public ChunkServerController(ChunkServerAccessService chunkServerAccessService) {
        this.chunkServerAccessService = chunkServerAccessService;
    }

    @GetMapping
    public ResponseEntity<List<ChunkServerEntity>> getChunkServers() {
        return ResponseEntity.ok().body(chunkServerAccessService.findAll());
    }

    @PostMapping("/heartBeat")
    public ResponseEntity<ChunkServerEntity> heartBeatUpdateServerStatus(@RequestBody HeartBeatRequest heartBeatRequest) {
        ChunkServerEntity storedEntity = chunkServerAccessService.makeServerDiscoverable(heartBeatRequest.ip,
                heartBeatRequest.port,
                heartBeatRequest.remainingStorageSize);
        return ResponseEntity.ok().body(storedEntity);
    }
}
