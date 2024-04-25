package com.ldfs.control.domain.repository;

import com.ldfs.control.domain.model.entity.ChunkServerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChunkServerEntityRepository extends JpaRepository<ChunkServerEntity, Long> {
}
