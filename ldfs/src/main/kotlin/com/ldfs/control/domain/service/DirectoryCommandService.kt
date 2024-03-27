package com.ldfs.control.domain.service

import com.ldfs.common.domain.AggregateAssociation
import com.ldfs.control.domain.common.toAggregate
import com.ldfs.control.domain.common.toEntity
import com.ldfs.control.domain.model.aggregate.Directory
import com.ldfs.control.domain.repository.DirectoryEntityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DirectoryCommandService(
    private val repository: DirectoryEntityRepository,
) {
    @Transactional
    fun create(
        name: String,
        parentDirectoryUuid: UUID?,
    ): Directory {
        return repository.save(
            Directory(
                name = name,
                parent =
                    parentDirectoryUuid?.let {
                        AggregateAssociation(it)
                    },
                children = emptyList(),
            ).toEntity(),
        ).toAggregate()
    }
}
