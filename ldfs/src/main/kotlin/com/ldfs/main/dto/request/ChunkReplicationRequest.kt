package com.ldfs.main.dto.request

import java.util.*

class ChunkReplicationRequest(
    var chunkUUID: UUID?,
    var chunkServerIp: String?,
    var chunkServerPort: String?,
    var fileId: UUID? = null,
    var chunkOrder: Long? = null,
)
