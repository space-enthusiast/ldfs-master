package com.ldfs.control.domain.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
class ChunkServerEntity(
    @Id
    var id: UUID? = null,

    val ip: String,

    val port: String,

    var remainingStorageSize: Long,

    var serverState: ServerState,
)