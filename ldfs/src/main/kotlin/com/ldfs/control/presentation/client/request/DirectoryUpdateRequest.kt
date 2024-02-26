package com.ldfs.control.presentation.client.request

import java.util.UUID

class DirectoryUpdateRequest(
    val name: String? = null,
    val parentUuid: UUID? = null,
)
