package com.ldfs.control.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class ChunkServerEntity {

    @Id
    private UUID id;

    public String ip;

    public String port;

    public Long remainingStorageSize;

    public ServerState serverState;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getRemainingStorageSize() {
        return remainingStorageSize;
    }

    public void setRemainingStorageSize(Long remainingStorageSize) {
        this.remainingStorageSize = remainingStorageSize;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public ServerState getServerState() {
        return serverState;
    }

    public void setServerState(ServerState serverState) {
        this.serverState = serverState;
    }
}
