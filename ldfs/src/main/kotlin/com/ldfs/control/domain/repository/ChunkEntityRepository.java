package com.ldfs.control.domain.repository;

import com.ldfs.control.domain.model.entity.ChunkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChunkEntityRepository extends JpaRepository<ChunkEntity, Long> {

    List<ChunkEntity> findAllByFileId(Long fileId);

    List<ChunkEntity> deleteAllByFileId(Long fileId);
}
