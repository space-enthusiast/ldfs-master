package com.ldfs.control.domain.service

import com.ldfs.common.domain.AggregateNotFoundException
import com.ldfs.control.domain.common.toAggregate
import com.ldfs.control.domain.model.aggregate.Directory
import com.ldfs.control.domain.repository.DirectoryEntityRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DirectoryQueryService(
    private val repository: DirectoryEntityRepository,
) {
    fun query(id: UUID): Directory {
        return repository.findByIdOrNull(id)?.toAggregate()
            ?: throw AggregateNotFoundException("directory not found id: $id")
    }

    fun queryLock(id: UUID): Directory {
        return repository.findAndPessimisticReadLockByIdOrNull(id)?.toAggregate()
            ?: throw AggregateNotFoundException(Directory::class.simpleName)
    }

    fun queryLock(ids: List<UUID>): List<Directory> {
        return repository.findAndPessimisticReadLockByIdsOrNull(ids).map {
            it.toAggregate()
        }.sortedBy { ids.indexOf(it.id) }.also { directories ->
            val inputIds = ids.toSet()
            val queriesIds = directories.map { it.id }.toSet()
            if (inputIds != queriesIds) {
                throw AggregateNotFoundException("${Directory::class.simpleName} ids: ${inputIds - queriesIds}")
            }
        }
    }
}
