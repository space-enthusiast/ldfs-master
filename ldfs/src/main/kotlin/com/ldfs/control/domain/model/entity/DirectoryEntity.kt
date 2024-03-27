package com.ldfs.control.domain.model.entity

import com.ldfs.common.jpa.StringListConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.OffsetDateTime
import java.util.UUID

@Entity
class DirectoryEntity(
    @Id
    var id: UUID,
    val created: OffsetDateTime,
    val updated: OffsetDateTime?,
    var name: String,
    var parent: UUID?,
    @Column(columnDefinition = "varchar")
    @Convert(converter = StringListConverter::class)
    var children: MutableList<UUID>,
)
