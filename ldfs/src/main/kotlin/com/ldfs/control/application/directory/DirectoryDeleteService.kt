package com.ldfs.control.application.directory

import com.ldfs.control.domain.service.DirectoryCommandService
import com.ldfs.control.domain.service.DirectoryQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DirectoryDeleteService(
    private val commandService: DirectoryCommandService,
    private val queryService: DirectoryQueryService,
) {
    @Transactional
    fun delete(directoryId: UUID) {
        validateDeletePossible(
            directoryId = directoryId,
        )
        commandService.delete(directoryId)
    }

    private fun validateDeletePossible(directoryId: UUID) {
        queryService.query(id = directoryId)
    }
}
