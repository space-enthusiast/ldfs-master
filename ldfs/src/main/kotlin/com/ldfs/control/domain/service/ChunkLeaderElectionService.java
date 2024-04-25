package com.ldfs.control.domain.service;

import com.ldfs.control.domain.model.entity.ChunkEntity;
import com.ldfs.control.domain.model.entity.ChunkServerEntity;
import com.ldfs.main.dto.LeaderFollowerChunkServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChunkLeaderElectionService {
    private final ChunkServerAccessService chunkServerAccessService;

    @Autowired
    public ChunkLeaderElectionService(ChunkServerAccessService chunkServerAccessService) {
        this.chunkServerAccessService = chunkServerAccessService;
    }

    public LeaderFollowerChunkServers electLeader(List<ChunkEntity> candidates) {
        ChunkEntity chosenChunk = candidates.get(temporaryLeaderElectionAlgorithm(candidates));
        candidates.remove(chosenChunk);
        ChunkServerEntity leader = chunkServerAccessService.findServerWithSpecificChunk(chosenChunk);
        List<ChunkServerEntity> followers = candidates.stream().map(chunkServerAccessService::findServerWithSpecificChunk).toList();
        return new LeaderFollowerChunkServers(leader, followers);
    }

    //TODO
    private static int temporaryLeaderElectionAlgorithm(List<ChunkEntity> candidates) {
        return (int) (Math.random() * candidates.size());
    }
}
