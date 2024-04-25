package com.ldfs.control.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
public class ChunkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long Id;

    public Long fileId;

    public Long sequence;

    public Long chunkServerId;

    @Setter
    public ChunkState stateChunk;

}
