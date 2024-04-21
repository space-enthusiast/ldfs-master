package com.ldfs.control.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ChunkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long Id;

    public Long fileId;

    public Long sequence;

    public ChunkState stateChunk;


    public void setStateChunk(ChunkState stateChunk) {
        this.stateChunk = stateChunk;
    }

}
