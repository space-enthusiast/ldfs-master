package com.ldfs.control.domain.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity

class ChunkEntity {
    @Id
    var Id: UUID? = null

    var fileUUID: UUID? = null

    var chunkOrder: Long? = null

    var chunkServerId: UUID? = null

    var stateChunk: ChunkState? = null
}