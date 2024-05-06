package com.ldfs.control.domain.service.leaderElection;

import com.ldfs.control.domain.model.entity.ChunkEntity;
import com.ldfs.control.domain.model.entity.ChunkServerEntity;
import com.ldfs.control.domain.model.entity.ChunkState;
import com.ldfs.control.domain.service.ChunkServerAccessService;
import com.ldfs.main.dto.LeaderFollowerChunkServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class ChunkLeaderElectionService {
    private final ChunkServerAccessService chunkServerAccessService;
    private final LeaderElectionRequestService leaderElectionRequestService;
    private final ExecutorService executorService;
    private final Map<String, CompletableFuture<ResponseEntity>> ongoingElections;
    private final int TIMEOUT_DURATION = 5000;

    @Autowired
    public ChunkLeaderElectionService(ChunkServerAccessService chunkServerAccessService, LeaderElectionRequestService leaderElectionRequestService, ExecutorService executorService, Map<String, CompletableFuture<ResponseEntity>> ongoingElections) {
        this.chunkServerAccessService = chunkServerAccessService;
        this.leaderElectionRequestService = leaderElectionRequestService;
        this.executorService = executorService;
        this.ongoingElections = ongoingElections;
    }

    public void electLeader(List<ChunkEntity> candidates) {
        List<ChunkServerEntity> broadCastList = candidates.stream().map(chunkServerAccessService::findServerWithSpecificChunk).toList();
        try {
            leaderElectionChunkStateLock(candidates);
            leaderElectionHTTPRequest(broadCastList);
        } catch (Exception e) {

        }
//        return new LeaderFollowerChunkServers(leader, followers);
    }

    private static void leaderElectionChunkStateLock(List<ChunkEntity> candidates) {
        for (ChunkEntity candidate : candidates) {
            candidate.setStateChunk(ChunkState.ELECTING);
        }
    }

    private void leaderElectionHTTPRequest(List<ChunkServerEntity> broadCastList) {
        // Generate checksum for the request
        String checksum = generateChecksum(broadCastList);

        // Create a CompletableFuture for the entire process
        CompletableFuture<ResponseEntity> future = CompletableFuture.supplyAsync(() -> {
            // Execute httpLeaderElectionService for each chunkServerEntity
//            this is here for purely a broadCast;
            broadCastList.forEach(chunkServerEntity -> {
                leaderElectionRequestService.httpLeaderElectionService(chunkServerEntity, broadCastList, checksum);
            });

            return null; // CompletableFuture<Void> requires a return value
        }).thenCompose(result -> {
            // Schedule a task to remove the checksum after a timeout
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
            scheduledExecutorService.schedule(() -> {
                CompletableFuture<ResponseEntity> electionFuture = ongoingElections.remove(checksum);
                if (electionFuture != null && !electionFuture.isDone()) {
                    electionFuture.completeExceptionally(new TimeoutException("Leader election timed out"));
                }
            }, TIMEOUT_DURATION, TimeUnit.SECONDS);

            return CompletableFuture.completedFuture(null);
        });


        ongoingElections.put(checksum, future);
        // Schedule a task to remove the checksum after a timeout

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

    //    isResponseReceived: Checks if a response for a particular checksum is ongoing,
    public boolean isElectionOngoing(String checksum) {
        return ongoingElections.containsKey(checksum);
    }
    //    retrieves the CompletableFuture associated with a given checksum,
//    allowing external components to track the progress or outcome of the leader election.
    public CompletableFuture<ResponseEntity> getElectionFuture(String checksum) {
        return ongoingElections.get(checksum);
    }

    //    completeElection: FORCE-Completes the leader election process for a given checksum by providing the corresponding response.
//    It removes the checksum from the map if it's still present and the associated CompletableFuture is not already completed.
    public CompletableFuture<ResponseEntity> forceCompleteElection(String checksum, ResponseEntity<ChunkEntity> response) {
        CompletableFuture<ResponseEntity> future = ongoingElections.remove(checksum);
        if (future != null && !future.isDone()) {
            future.complete(response);
        }
        return future;
    }
}
