package com.ldfs.control.application.directory

import com.ldfs.common.domain.AggregateAssociation
import com.ldfs.common.domain.AggregateNotFoundException
import com.ldfs.control.domain.model.aggregate.Directory
import com.ldfs.control.domain.service.DirectoryCommandService
import com.ldfs.control.domain.service.DirectoryQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DirectoryUpdateService(
    private val commandService: DirectoryCommandService,
    private val queryService: DirectoryQueryService,
) {
    @Transactional
    fun update(
        directoryId: UUID,
        name: String?,
        parentDirectoryId: UUID?,
    ): Directory {
        validateUpdatePossible(
            name = name,
            parentDirectoryId = parentDirectoryId,
        )
        val (lockedDirectory, lockedParentDirectory) =
            queryAndLockDirectoryAndParentDirectory(
                directoryId = directoryId,
                parentDirectoryId = parentDirectoryId,
            )
        val lockedOriginalParentDirectory =
            lockedDirectory.parent?.let {
                queryService.queryLock(it.id)
            }

        lockedParentDirectory?.let {
            lockedDirectory.parent = AggregateAssociation(it.id)
        }

        name?.let {
            lockedDirectory.name = it
        }

        lockedOriginalParentDirectory?.let {
            it.children = it.children.filter { child -> child.id != directoryId }
        }

        lockedParentDirectory?.let {
            it.children = it.children + AggregateAssociation(directoryId)
        }

        return commandService.saveAll(
            listOfNotNull(lockedDirectory, lockedParentDirectory, lockedOriginalParentDirectory),
        ).firstOrNull() ?: throw AggregateNotFoundException("${Directory::class.simpleName} id: $directoryId")
    }

    private fun queryAndLockDirectoryAndParentDirectory(
        directoryId: UUID,
        parentDirectoryId: UUID?,
    ): Pair<Directory, Directory?> {
        return if (parentDirectoryId == null) {
            Pair(
                queryService.queryLock(directoryId),
                null,
            )
        } else {
            val lockedDirectories = queryService.queryLock(listOf(directoryId, parentDirectoryId))
            Pair(
                lockedDirectories[0],
                lockedDirectories[1],
            )
        }
    }

    private fun validateUpdatePossible(
        name: String?,
        parentDirectoryId: UUID?,
    ) {
        if (name == "") {
            throw RuntimeException("directory name should not be blank")
        }

        parentDirectoryId?.let {
            queryService.query(id = parentDirectoryId)
        }
    }
}
