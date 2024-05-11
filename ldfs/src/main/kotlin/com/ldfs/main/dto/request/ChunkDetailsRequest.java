package com.ldfs.main.dto.request;

import java.util.UUID;

public class ChunkDetailsRequest {
    public UUID chunkUUID;
    public String chunkServerIp;
    public String chunkServerPort;
    public Long chunkOrder;

    public ChunkDetailsRequest() {
        //default constructor
    }

    public UUID getChunkUUID() {
        return chunkUUID;
    }

    public void setChunkUUID(UUID chunkUUID) {
        this.chunkUUID = chunkUUID;
    }

    public String getChunkServerPort() {
        return chunkServerPort;
    }

    public void setChunkServerPort(String chunkServerPort) {
        this.chunkServerPort = chunkServerPort;
    }

    public Long getChunkOrder() {
        return chunkOrder;
    }

    public void setChunkOrder(Long chunkOrder) {
        this.chunkOrder = chunkOrder;
    }

    public String getChunkServerIp() {
        return chunkServerIp;
    }

    public void setChunkServerIp(String chunkServerIp) {
        this.chunkServerIp = chunkServerIp;
    }
}
