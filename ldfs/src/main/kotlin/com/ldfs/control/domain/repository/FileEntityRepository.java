package com.ldfs.control.domain.repository;

import com.ldfs.control.domain.model.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileEntityRepository extends JpaRepository<FileEntity, UUID> {
}
