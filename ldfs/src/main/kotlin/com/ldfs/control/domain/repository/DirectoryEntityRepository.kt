package com.ldfs.control.domain.repository

import com.ldfs.control.domain.model.entity.DirectoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DirectoryEntityRepository : JpaRepository<DirectoryEntity, UUID>
