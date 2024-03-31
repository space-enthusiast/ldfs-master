package com.ldfs.control.application.directory

import com.ldfs.control.domain.service.DirectoryCommandService
import com.ldfs.control.domain.service.DirectoryQueryService
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DirectoryDeleteService(
    private val commandService: DirectoryCommandService,
    private val queryService: DirectoryQueryService,
) {
    @Transactional
    fun delete(
        directoryId: UUID,
    ) {
        validateDeletePossible(
            directoryId = directoryId,
        )
        val lockedDirectory = queryService.queryLock(directoryId)
        val lockedOriginalParentDirectory = lockedDirectory.parent?.let {
            queryService.queryLock(it.id)
        }

        lockedOriginalParentDirectory?.let {
            it.children = it.children.filter { child -> child.id != directoryId }
        }
        lockedOriginalParentDirectory?.let {
            commandService.save(it)
        }
        commandService.delete(directoryId)
    }

    private fun validateDeletePossible(
        directoryId: UUID,
    ) {
        queryService.query(id = directoryId)
    }
}
