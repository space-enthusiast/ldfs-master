package com.ldfs.control.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.net.InetAddress;

@Entity
@Data
public class ChunkServerEntity {

    @Id
    private Long id;

    public String ip;

    public String port;

    public ServerState serverState;

}
