package com.ldfs.control.domain.repository

import com.ldfs.control.domain.model.entity.DirectoryEntity
import jakarta.persistence.LockModeType
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface DirectoryEntityRepository : JpaRepository<DirectoryEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select d from directory d")
    fun findAndPessimisticReadLockByIdOrNull(id: UUID): DirectoryEntity?

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select d from directory d where d.id in :ids")
    fun findAndPessimisticReadLockByIdsOrNull(@Param("ids") ids: List<UUID>): List<DirectoryEntity>
}
