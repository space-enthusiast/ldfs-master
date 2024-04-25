package com.ldfs.main.dto;

import com.ldfs.control.domain.model.entity.ChunkServerEntity;

import java.util.List;

public record LeaderFollowerChunkServers(ChunkServerEntity leased, List<ChunkServerEntity> nonLeased) {
}
