package com.ldfs.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateFileResponse {
    private Long order;
    private String chunkServerIP;
    private String chunkServerPort;

    public CreateFileResponse() {
        // Default constructor
    }

    public CreateFileResponse(Long order, String chunkServerPort, String chunkServerIP) {
        this.order = order;
        this.chunkServerPort = chunkServerPort;
        this.chunkServerIP = chunkServerIP;
    }

    public String getChunkServerPort() {
        return chunkServerPort;
    }

    public void setChunkServerPort(String chunkServerPort) {
        this.chunkServerPort = chunkServerPort;
    }

    public String getChunkServerIP() {
        return chunkServerIP;
    }

    public void setChunkServerIP(String chunkServerIP) {
        this.chunkServerIP = chunkServerIP;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }
}
