package com.ldfs.presentation.dto.request

import java.util.UUID

class CreateFileRequest(
    var fileUUID: UUID? = null,
    var fileSize: Long? = null,
    var fileName: String? = null,
    var directoryId: UUID? = null,
)
