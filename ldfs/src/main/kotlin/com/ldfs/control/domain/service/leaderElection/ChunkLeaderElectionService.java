package com.ldfs.control.domain.service.leaderElection;

import com.ldfs.control.domain.model.entity.ChunkEntity;
import com.ldfs.control.domain.model.entity.ChunkServerEntity;
import com.ldfs.control.domain.model.entity.ChunkState;
import com.ldfs.control.domain.service.ChunkServerAccessService;
import com.ldfs.main.dto.LeaderFollowerChunkServers;
import kotlin.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class ChunkLeaderElectionService {
    private final ChunkServerAccessService chunkServerAccessService;
    private final LeaderElectionRequestService leaderElectionRequestService;
    private final ConcurrentHashMap<String, Pair<String, Pair<InetSocketAddress, UUID>>> requestState = new ConcurrentHashMap<>();

    @Autowired
    public ChunkLeaderElectionService(ChunkServerAccessService chunkServerAccessService,
                                      LeaderElectionRequestService leaderElectionRequestService) {
        this.chunkServerAccessService = chunkServerAccessService;
        this.leaderElectionRequestService = leaderElectionRequestService;
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
        return requestCheckSum;
    }

    //    1, sendAsyncLeaderElectionRequest
//    2, getLeaderElectedChunkServer
    public Pair<InetSocketAddress, UUID> getLeaderElectedChunkServer(String requestCheckSum) {
        CompletableFuture<Pair<InetSocketAddress, UUID>> future = new CompletableFuture<>();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            if (requestState.containsKey(requestCheckSum) && requestState.get(requestCheckSum).getFirst().equals(successState)) {
                future.complete(requestState.get(requestCheckSum).getSecond()); // Complete the future when result is found
            }
        }, 0, 1, TimeUnit.SECONDS); // Initial delay of 0, execute every 1 second

        // Optionally, you can cancel the future after a certain duration
        executor.schedule(() -> future.cancel(false), 10, TimeUnit.SECONDS); // Cancel after 10 seconds

        try {
            return future.get(); // Wait for the future to complete and return the result
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            executor.shutdown(); // Shut down the executor
        }
    }

    // api that the chunk server uses to declare itself the leader
    public void setChunkServerAsLeader(InetSocketAddress chunkServer, UUID chunkUuid, String requestCheckSum) {
        String state = requestState.get(requestCheckSum).getFirst();
        if (state != null && state.equals(waitingState)) {
            requestState.put(requestCheckSum, new Pair<>(successState, new Pair<>(chunkServer, chunkUuid)));
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
