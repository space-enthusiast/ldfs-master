package com.ldfs.main.controller;

import com.ldfs.control.domain.model.entity.ChunkEntity;
import com.ldfs.control.domain.repository.ChunkServerEntityRepository;
import com.ldfs.control.domain.service.ChunkAccessService;
import com.ldfs.control.domain.service.FileAccessService;
import com.ldfs.control.domain.service.leaderElection.ChunkLeaderElectionService;
import com.ldfs.main.dto.request.CreateFileRequest;
import com.ldfs.main.dto.response.AppendUpdateResponse;
import com.ldfs.main.dto.response.CreateFileResponse;
import com.ldfs.main.dto.LeaderFollowerChunkServers;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class FileAndChunkAccessController {

    private final ChunkAccessService chunkAccessService;
    private final ChunkLeaderElectionService leaderElectionService;
    private final FileAccessService fileAccessService;
    private final ChunkServerEntityRepository chunkServerEntityRepository;
    @Autowired
    public FileAndChunkAccessController(ChunkAccessService chunkAccessService, ChunkLeaderElectionService leaderElectionService, FileAccessService fileAccessService, ChunkServerEntityRepository chunkServerEntityRepository) {
        this.chunkAccessService = chunkAccessService;
        this.leaderElectionService = leaderElectionService;
        this.fileAccessService = fileAccessService;
        this.chunkServerEntityRepository = chunkServerEntityRepository;
    }

    @GetMapping("/getFile")
    public ResponseEntity<List<ChunkEntity>> getFile(@RequestParam("fileId") UUID fileId) {
        // Logic to retrieve file chunks based on fileId
        // Dummy response for demonstration
        List<ChunkEntity> response = chunkAccessService.getFileChunks(fileId);
        // Populate response with dummy data
        return ResponseEntity.ok(response);
    }

    @PostMapping("/fileCreateOperation")
    public ResponseEntity<List<CreateFileResponse>> createFile(@RequestBody CreateFileRequest request) {
        // Logic to create a new file
        // Dummy response for demonstration
        fileAccessService.save(request.fileName, request.directoryId, request.fileUuid);
        return ResponseEntity.ok().body(fileAccessService.chunkify(request.fileSize));
    }

    @DeleteMapping("/deleteFile")
    public ResponseEntity<String> deleteFile(@RequestParam("fileId") UUID fileId) {
        // Logic to delete a file
        // Dummy response for demonstration
        chunkAccessService.deleteFile(fileId);
        return ResponseEntity.ok("File deleted successfully");
    }

    //TODO
    @PostMapping("/appendUpdateFile")
    public ResponseEntity<AppendUpdateResponse> appendUpdateFile(@RequestParam UUID fileId, @RequestParam UUID chunkId) {
        // Logic to append or update a file
        List<ChunkEntity> candidates = chunkAccessService.getSpecificChunkOfFile(fileId, chunkId);
        LeaderFollowerChunkServers result = leaderElectionService.mockElectLeader(candidates);
        // Dummy response for demonstration
        AppendUpdateResponse response = new AppendUpdateResponse(result);
        // Populate response with dummy data
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/fail")
    public ResponseEntity<String> fail(@RequestParam("fileId") UUID fileId) {
        fileAccessService.markFail(fileId);
        return ResponseEntity.noContent().build();
    }



}
