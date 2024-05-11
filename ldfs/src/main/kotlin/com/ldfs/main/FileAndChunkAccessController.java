package com.ldfs.main;

import com.ldfs.control.domain.model.entity.ChunkEntity;
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
    @Autowired
    public FileAndChunkAccessController(ChunkAccessService chunkAccessService, ChunkLeaderElectionService leaderElectionService, FileAccessService fileAccessService) {
        this.chunkAccessService = chunkAccessService;
        this.leaderElectionService = leaderElectionService;
        this.fileAccessService = fileAccessService;
    }

    @GetMapping("/getFile")
    public ResponseEntity<List<ChunkEntity>> getFile(@RequestParam("fileId") UUID fileId) {
        // Logic to retrieve file chunks based on fileId
        // Dummy response for demonstration
        List<ChunkEntity> response = chunkAccessService.getFileChunks(fileId);
        // Populate response with dummy data
        return ResponseEntity.ok(response);
    }

    @Transactional
    @PostMapping("/createFile")
    public ResponseEntity<List<CreateFileResponse>> createFile(@RequestBody CreateFileRequest request) {
        // Logic to create a new file
        // Dummy response for demonstration
        fileAccessService.save(request.fileName, request.directoryId, request.fileUUID);
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
    public ResponseEntity<AppendUpdateResponse> appendUpdateFile(@RequestParam UUID fileId, @RequestParam Long ChunkId) {
        // Logic to append or update a file
        List<ChunkEntity> candidates = chunkAccessService.getSpecificChunkOfFile(fileId, ChunkId);
        LeaderFollowerChunkServers result = leaderElectionService.mockElectLeader(candidates);
        // Dummy response for demonstration
        AppendUpdateResponse response = new AppendUpdateResponse(result);
        // Populate response with dummy data
        return ResponseEntity.ok(response);
    }



}
