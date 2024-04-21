package com.ldfs.main;

import com.ldfs.control.domain.model.entity.ChunkEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class MainController {

    private final ChunkAccessService chunkAccessService;
    @Autowired
    public MainController(ChunkAccessService chunkAccessService) {
        this.chunkAccessService = chunkAccessService;
    }

    @GetMapping("/getFile")
    public ResponseEntity<List<ChunkEntity>> getFile(@RequestParam("fileId") Long fileId) {
        // Logic to retrieve file chunks based on fileId
        // Dummy response for demonstration
        List<ChunkEntity> response = chunkAccessService.getFileChunks(fileId);
        // Populate response with dummy data
        return ResponseEntity.ok(response);
    }

    @PostMapping("/createFile")
    public ResponseEntity<String> createFile() {
        // Logic to create a new file
        // Dummy response for demonstration
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("this feature is not yet ready");
    }

    @DeleteMapping("/deleteFile")
    public ResponseEntity<String> deleteFile(@RequestParam("fileId") Long fileId) {
        // Logic to delete a file
        // Dummy response for demonstration
        chunkAccessService.deleteFile(fileId);
        return ResponseEntity.ok("File deleted successfully");
    }

    //TODO
//    @PostMapping("/appendUpdateFile")
//    public ResponseEntity<AppendUpdateResponse> appendUpdateFile(@RequestBody Long fileId) {
//        // Logic to append or update a file
//        // Dummy response for demonstration
//        AppendUpdateResponse response = new AppendUpdateResponse();
//        // Populate response with dummy data
//        return ResponseEntity.ok(response);
//    }

}
