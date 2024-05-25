package com.ldfs.control.domain.repository

import com.ldfs.control.domain.model.entity.ChunkServerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ChunkServerEntityRepository : JpaRepository<ChunkServerEntity, Long> {
    fun findByIp(IpAddress: String?): ChunkServerEntity?

    fun findByIpAndPort(IpAddress: String?, port: String?): ChunkServerEntity?

    @Query(value = "SELECT i FROM ChunkServerEntity i WHERE i.remainingStorageSize = (SELECT MAX(i.remainingStorageSize) FROM ChunkServerEntity i)")
    fun findByLargestRemainingStorageSize(): List<ChunkServerEntity?>?
}