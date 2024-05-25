package com.ldfs.main.dto.request

import java.util.*


class CreateFileRequest (
    var fileUUID: UUID? = null,
    var fileSize: Long? = null,
    var fileName: String? = null,
    var directoryId: UUID? = null,
)