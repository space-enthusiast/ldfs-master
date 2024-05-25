package com.ldfs.control.domain.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import lombok.Data
import java.util.UUID

@Data
@Entity
class FileEntity {
    @Id
    var fileUUID: UUID? = null
    var name: String? = null
    var directoryId: UUID? = null
    var status: String? = null
}
