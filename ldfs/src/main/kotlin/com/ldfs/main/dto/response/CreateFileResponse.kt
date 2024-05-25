package com.ldfs.main.dto.response

data class CreateFileResponse(
    var order: Long? = null,
    var chunkServerIP: String? = null,
    var chunkServerPort: String? = null,
)
