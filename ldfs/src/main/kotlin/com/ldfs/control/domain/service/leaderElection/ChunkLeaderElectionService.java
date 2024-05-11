package com.ldfs.control.domain.service.leaderElection;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ldfs.control.domain.model.entity.ChunkEntity;
import com.ldfs.control.domain.model.entity.ChunkServerEntity;
import com.ldfs.control.domain.model.entity.ChunkState;
import com.ldfs.control.domain.service.ChunkServerAccessService;
import com.ldfs.main.dto.LeaderFollowerChunkServers;
import kotlin.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class ChunkLeaderElectionService {
    private final ChunkServerAccessService chunkServerAccessService;
    private final LeaderElectionRequestService leaderElectionRequestService;
    private final Cache<String, Pair<String, ChunkServerEntity>> activeLeaderElections;

    @Autowired
    public ChunkLeaderElectionService(ChunkServerAccessService chunkServerAccessService,
                                      LeaderElectionRequestService leaderElectionRequestService) {
        this.chunkServerAccessService = chunkServerAccessService;
        this.leaderElectionRequestService = leaderElectionRequestService;
        this.activeLeaderElections = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build();
    }

    // ------------------------------------------------------------

    /*
        ongoing request leader election request checksum to state or value
    */
    private final String successState = "SUCCESS";
    private final String waitingState = "WAITING";


    //    initiate leader election
    public String sendAsyncLeaderElectionRequest(List<ChunkServerEntity> broadCastList) {
        String requestCheckSum = generateChecksum(broadCastList);
        for (int i = 0; i < broadCastList.size(); i++) {
            leaderElectionRequestService.httpLeaderElectionService(broadCastList.get(i), broadCastList, requestCheckSum);
        }

        Pair<String, ChunkServerEntity> leaderElectionDetails = new Pair<>(waitingState, null);
        activeLeaderElections.put(requestCheckSum, leaderElectionDetails);

        return requestCheckSum;
    }

    public Pair<String, ChunkServerEntity> getLeaderElectedChunkServer(String requestCheckSum) {
        return activeLeaderElections.getIfPresent(requestCheckSum);

    }

    // api that the chunk server uses to declare itself the leader
    public void setChunkServerAsLeader(InetSocketAddress chunkServer, UUID chunkUuid, String requestCheckSum) {
        Pair<String, ChunkServerEntity> state = activeLeaderElections.getIfPresent(requestCheckSum);
        if (state != null && state.getFirst().equals(waitingState)) {
            ChunkServerEntity chunkServerEntity = chunkServerAccessService.findServerWithInetSocketAddr(chunkServer);
            Pair<String, ChunkServerEntity> newState = new Pair<>(successState, chunkServerEntity);
            activeLeaderElections.put(requestCheckSum, newState);
        }
    }

    // ------------------------------------------------------------

    public void electLeader(List<ChunkEntity> candidates) {
        List<ChunkServerEntity> broadCastList = candidates.stream().map(chunkServerAccessService::findServerWithSpecificChunk).toList();
        try {
            leaderElectionChunkStateLock(candidates);
            String checksum = sendAsyncLeaderElectionRequest(broadCastList);
            getLeaderElectedChunkServer(checksum);

        } catch (Exception e) {

        }
    }

    private static void leaderElectionChunkStateLock(List<ChunkEntity> candidates) {
        for (ChunkEntity candidate : candidates) {
            candidate.setStateChunk(ChunkState.ELECTING);
        }
    }


    public LeaderFollowerChunkServers mockElectLeader(List<ChunkEntity> candidates) {
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

    private String generateChecksum(List<ChunkServerEntity> data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.toString().getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle exception
            return null;
        }
    }
}