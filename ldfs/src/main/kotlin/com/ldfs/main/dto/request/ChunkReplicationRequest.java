package com.ldfs.main.dto.request;

import java.util.List;
import java.util.UUID;

public class ChunkReplicationRequest {
    public UUID fileId;
    public List<ChunkDetailsRequest> fileChunks;

    public ChunkReplicationRequest() {
        //default constructor
    }

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public List<ChunkDetailsRequest> getFileChunks() {
        return fileChunks;
    }

    public void setFileChunks(List<ChunkDetailsRequest> fileChunks) {
        this.fileChunks = fileChunks;
    }
}
