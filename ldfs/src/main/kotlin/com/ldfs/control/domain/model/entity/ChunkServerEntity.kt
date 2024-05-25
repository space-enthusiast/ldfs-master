package com.ldfs.control.domain.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
class ChunkServerEntity(
    @Id
    var id: UUID? = null,

    var ip: String = "",

    var port: String = "",

    var remainingStorageSize: Long = 0L,

    var serverState: ServerState = ServerState.UNKNOWN,
) {

}