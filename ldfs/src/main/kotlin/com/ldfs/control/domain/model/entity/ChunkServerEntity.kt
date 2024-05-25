package com.ldfs.control.domain.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
class ChunkServerEntity {
    @Id
    private val id: UUID? = null

    var ip: String? = null

    var port: String? = null

    var remainingStorageSize: Long? = null

    var serverState: ServerState? = null
}