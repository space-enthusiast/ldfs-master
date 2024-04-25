package com.ldfs.control.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ChunkServerEntity {

    @Id
    private Long id;
    public ServerState serverState;

}
