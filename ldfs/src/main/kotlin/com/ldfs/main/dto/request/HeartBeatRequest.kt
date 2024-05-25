package com.ldfs.main.dto.request


class HeartBeatRequest (
    var ip: String? = null,
    var port: String? = null,
    var remainingStorageSize: Long? = null,
)