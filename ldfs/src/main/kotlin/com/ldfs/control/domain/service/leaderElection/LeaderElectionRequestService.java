package com.ldfs.control.domain.service.leaderElection;

import com.ldfs.control.domain.model.entity.ChunkEntity;
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


    @Autowired
    public LeaderElectionRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Async
    public void httpLeaderElectionService(ChunkServerEntity chunkServer, List<ChunkServerEntity> broadCastable, String checksum) {

            String url = "http://" + chunkServer.getIp() + ":" + chunkServer.getPort() + "/";
            RequestBodyDTO requestBody = new RequestBodyDTO(broadCastable, checksum);
            restTemplate.postForObject(url, requestBody, Void.class);
    }


}
