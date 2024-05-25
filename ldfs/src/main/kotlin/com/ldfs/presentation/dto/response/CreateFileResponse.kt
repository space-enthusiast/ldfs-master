package com.ldfs.presentation.dto.response

data class CreateFileResponse(
    var order: Long? = null,
    var chunkServerIP: String? = null,
    var chunkServerPort: String? = null,
)
