package com.ldfs.control.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class ChunkEntity {
    @Id
    public UUID Id;

    public UUID fileUUID;

    public Long chunkOrder;

    public UUID chunkServerId;

    public ChunkState stateChunk;

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public ChunkState getStateChunk() {
        return stateChunk;
    }

    public void setStateChunk(ChunkState stateChunk) {
        this.stateChunk = stateChunk;
    }

    public UUID getChunkServerId() {
        return chunkServerId;
    }

    public void setChunkServerId(UUID chunkServerId) {
        this.chunkServerId = chunkServerId;
    }

    public Long getChunkOrder() {
        return chunkOrder;
    }

    public void setChunkOrder(Long sequence) {
        this.chunkOrder = sequence;
    }

    public UUID getFileUUID() {
        return fileUUID;
    }

    public void setFileUUID(UUID fileId) {
        this.fileUUID = fileId;
    }
}
