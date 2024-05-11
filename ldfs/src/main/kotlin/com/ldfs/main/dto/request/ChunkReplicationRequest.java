package com.ldfs.main.dto.request;

import java.util.UUID;

public class ChunkReplicationRequest {
    public UUID chunkUUID;
    public String chunkServerIp;
    public String chunkServerPort;
    public UUID fileId;
    public Long chunkOrder;

    public ChunkReplicationRequest(UUID chunkUUID, Long chunkOrder, UUID fileId, String chunkServerPort, String chunkServerIp) {
        this.chunkUUID = chunkUUID;
        this.chunkOrder = chunkOrder;
        this.fileId = fileId;
        this.chunkServerPort = chunkServerPort;
        this.chunkServerIp = chunkServerIp;
    }

    public ChunkReplicationRequest(){
        //default constructor
    }

    public UUID getChunkUUID() {
        return chunkUUID;
    }

    public void setChunkUUID(UUID chunkUUID) {
        this.chunkUUID = chunkUUID;
    }

    public Long getChunkOrder() {
        return chunkOrder;
    }

    public void setChunkOrder(Long chunkOrder) {
        this.chunkOrder = chunkOrder;
    }

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public String getChunkServerPort() {
        return chunkServerPort;
    }

    public void setChunkServerPort(String chunkServerPort) {
        this.chunkServerPort = chunkServerPort;
    }

    public String getChunkServerIp() {
        return chunkServerIp;
    }

    public void setChunkServerIp(String chunkServerIp) {
        this.chunkServerIp = chunkServerIp;
    }
}
