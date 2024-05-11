package com.ldfs.main.dto.response;

import com.ldfs.control.domain.model.entity.ChunkServerEntity;
import com.ldfs.main.dto.LeaderFollowerChunkServers;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
public class AppendUpdateResponse {
    List<ChunkServerEntity> nonleasedServer;
    ChunkServerEntity leasedServer;

    public AppendUpdateResponse(ChunkServerEntity leasedServer, List<ChunkServerEntity> nonleasedServer) {
        this.nonleasedServer = nonleasedServer;
        this.leasedServer = leasedServer;
    }

    public AppendUpdateResponse(LeaderFollowerChunkServers record) {
        this.leasedServer = record.leased();
        this.nonleasedServer = record.nonLeased();
    }
}
