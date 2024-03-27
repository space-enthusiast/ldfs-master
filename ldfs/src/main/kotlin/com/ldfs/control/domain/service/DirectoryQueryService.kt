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
}
