package com.ldfs.main;

import com.ldfs.main.dto.request.ChunkReplicationRequest;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files/replication")
public class FileReplicationController {

    @PostMapping("/chunkReplicationInit")
    @Transactional
    public void chunkReplicationInit(@RequestBody ChunkReplicationRequest chunkReplicationRequest) {

    }
}
