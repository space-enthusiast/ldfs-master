package com.ldfs.control.domain.service.leaderElection

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.ldfs.control.domain.model.entity.ChunkEntity
import com.ldfs.control.domain.model.entity.ChunkServerEntity
import com.ldfs.control.domain.model.entity.ChunkState
import com.ldfs.control.domain.service.ChunkServerAccessService
import com.ldfs.main.dto.LeaderFollowerChunkServers
import org.springframework.stereotype.Service
import java.net.InetSocketAddress
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class ChunkLeaderElectionService (
    private val chunkServerAccessService: ChunkServerAccessService,
    private val leaderElectionRequestService: LeaderElectionRequestService
) {
    private val activeLeaderElections: Cache<String?, Pair<String, ChunkServerEntity?>> =
        Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build()

    // ------------------------------------------------------------
    /*
        ongoing request leader election request checksum to state or value
    */
    private val successState = "SUCCESS"
    private val waitingState = "WAITING"

    //    initiate leader election
    fun sendAsyncLeaderElectionRequest(broadCastList: List<ChunkServerEntity>): String? {
        val requestCheckSum = generateChecksum(broadCastList)
        for (i in broadCastList.indices) {
            leaderElectionRequestService.httpLeaderElectionService(broadCastList[i], broadCastList, requestCheckSum)
        }

        val leaderElectionDetails = Pair<String, ChunkServerEntity?>(waitingState, null)
        activeLeaderElections.put(requestCheckSum, leaderElectionDetails)

        return requestCheckSum
    }

    fun getLeaderElectedChunkServer(requestCheckSum: String?): Pair<String, ChunkServerEntity?> {
        return activeLeaderElections.getIfPresent(requestCheckSum)!!
    }

    // api that the chunk server uses to declare itself the leader
    fun setChunkServerAsLeader(chunkServer: InetSocketAddress?, chunkUuid: UUID?, requestCheckSum: String?) {
        val state = activeLeaderElections.getIfPresent(requestCheckSum)
        if (state != null && state.first == waitingState) {
            val chunkServerEntity = chunkServerAccessService.findServerWithInetSocketAddr(chunkServer)
            val newState = Pair(successState, chunkServerEntity)
            activeLeaderElections.put(requestCheckSum, newState)
        }
    }

    // ------------------------------------------------------------
    fun electLeader(candidates: List<ChunkEntity>) {
        val broadCastList = candidates.stream().map { chunk: ChunkEntity? ->
            chunkServerAccessService.findServerWithSpecificChunk(
                chunk
            )
        }.toList()
        try {
            leaderElectionChunkStateLock(candidates)
            val checksum = sendAsyncLeaderElectionRequest(broadCastList)
            getLeaderElectedChunkServer(checksum)
        } catch (e: Exception) {
        }
    }

    fun mockElectLeader(candidates: MutableList<ChunkEntity>): LeaderFollowerChunkServers {
        val chosenChunk = candidates[temporaryLeaderElectionAlgorithm(candidates)]
        candidates.remove(chosenChunk)
        val leader = chunkServerAccessService.findServerWithSpecificChunk(chosenChunk)
        val followers = candidates.stream().map { chunk: ChunkEntity? ->
            chunkServerAccessService.findServerWithSpecificChunk(
                chunk
            )
        }.toList()
        return LeaderFollowerChunkServers(leader, followers)
    }

    private fun generateChecksum(data: List<ChunkServerEntity>): String? {
        try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(data.toString().toByteArray())
            return Base64.getEncoder().encodeToString(hash)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            // Handle exception
            return null
        }
    }

    private fun leaderElectionChunkStateLock(candidates: List<ChunkEntity>) {
        for (candidate in candidates) {
            candidate.stateChunk = ChunkState.ELECTING
        }
    }


        //TODO
    private fun temporaryLeaderElectionAlgorithm(candidates: List<ChunkEntity>): Int {
        return (Math.random() * candidates.size).toInt()
    }
}