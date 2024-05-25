package com.ldfs.control.domain.service

import com.ldfs.control.domain.model.entity.ChunkEntity
import com.ldfs.control.domain.model.entity.ChunkState
import com.ldfs.control.domain.repository.ChunkEntityRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Consumer

@Service
class ChunkAccessService @Autowired constructor(private val chunkEntityRepository: ChunkEntityRepository) {
    fun getFileChunks(fileId: UUID?): List<ChunkEntity> {
        return chunkEntityRepository.findAllByFileUUID(fileId!!)
    }

    fun getSpecificChunkOfFile(fileId: UUID?, chunkId: Long?): List<ChunkEntity> {
        return chunkEntityRepository.findAllByFileUUID(fileId!!)
    }

    fun deleteFile(fileId: UUID) {
        //TODO
        val deleteList = chunkEntityRepository.findAllByFileUUID(fileId)
        if (deleteList.isEmpty()) {
            throw EntityNotFoundException("File with ID" + fileId + "is not found within DB")
        }
        deleteList.forEach(Consumer { chunkEntity: ChunkEntity ->
            chunkEntity.stateChunk = ChunkState.DELETING
        })
    }
}
