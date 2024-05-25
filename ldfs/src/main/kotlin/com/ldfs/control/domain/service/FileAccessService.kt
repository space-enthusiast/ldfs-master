package com.ldfs.control.domain.service

import com.ldfs.control.domain.model.entity.FileEntity
import com.ldfs.control.domain.repository.FileEntityRepository
import com.ldfs.presentation.dto.response.CreateFileResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FileAccessService
    @Autowired
    constructor(
        private val fileEntityRepository: FileEntityRepository,
        private val chunkServerAccessService: ChunkServerAccessService,
    ) {
        private val chunkSize = 64L // MB

        fun save(
            name: String?,
            directoryId: UUID?,
            fileUUID: UUID?,
        ) {
            val fileEntity = FileEntity()
            fileEntity.name = name
            fileEntity.fileUUID = fileUUID
            fileEntity.directoryId = directoryId
            fileEntity.status = "CREATING"
            fileEntityRepository.save(fileEntity)
        }

        fun chunkify(fileSize: Long): List<CreateFileResponse> {
            val totalChunkNumber = (fileSize / chunkSize).toInt() + 1
            return chunkServerAccessService.findServersWithStorageSpace(totalChunkNumber)
        }
    }
