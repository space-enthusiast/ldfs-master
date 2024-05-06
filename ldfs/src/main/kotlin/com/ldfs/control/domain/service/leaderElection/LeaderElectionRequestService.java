package com.ldfs.control.domain.service.leaderElection;

import com.ldfs.control.domain.model.entity.ChunkServerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class LeaderElectionRequestService {

    private static class RequestBodyDTO {
        List<ChunkServerEntity> chunkServerEntities;
        String checksum;

        RequestBodyDTO(List<ChunkServerEntity> chunkServerEntities, String checksum) {
            this.chunkServerEntities = chunkServerEntities;
            this.checksum = checksum;
        }
    }

    private final RestTemplate restTemplate;
    private final ExecutorService executorService;
    private final Map<String, CompletableFuture<ResponseEntity>> ongoingElections;
    private final int TIMEOUT_DURATION = 5000;

    @Autowired
    public LeaderElectionRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.executorService = Executors.newCachedThreadPool();
        this.ongoingElections = new ConcurrentHashMap<>();
    }


    @Async
    public void httpLeaderElectionService(ChunkServerEntity chunkServer, List<ChunkServerEntity> broadCastable, String checksum) {
        try {
            String url = "http://" + chunkServer.getIp() + ":" + chunkServer.getPort() + "/";
            RequestBodyDTO requestBody = new RequestBodyDTO(broadCastable, checksum);
            restTemplate.postForObject(url, requestBody, Void.class);
            CompletableFuture<ResponseEntity> future = new CompletableFuture<>();
            ongoingElections.put(checksum, future);
            // Schedule a task to remove the checksum after a timeout
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
            scheduledExecutorService.schedule(() -> {
                CompletableFuture<ResponseEntity> electionFuture = ongoingElections.remove(checksum);
                if (electionFuture != null && !electionFuture.isDone()) {
                    electionFuture.completeExceptionally(new TimeoutException("Leader election timed out"));
                }
            }, TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (Exception e) {
            // Handle exception
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

//    completeElection: Completes the leader election process for a given checksum by providing the corresponding response.
//    It removes the checksum from the map if it's still present and the associated CompletableFuture is not already completed.
    public void completeElection(String checksum, ResponseEntity response) {
        CompletableFuture<ResponseEntity> future = ongoingElections.remove(checksum);
        if (future != null && !future.isDone()) {
            future.complete(response);
        }
    }

}
