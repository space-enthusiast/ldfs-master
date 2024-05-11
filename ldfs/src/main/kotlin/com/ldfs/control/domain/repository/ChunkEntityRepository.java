package com.ldfs.control.domain.repository;

import com.ldfs.control.domain.model.entity.ChunkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChunkEntityRepository extends JpaRepository<ChunkEntity, UUID> {

    List<ChunkEntity> findAllByFileUUID(UUID FileUUID);

    List<ChunkEntity> findAllByFileUUIDAndChunkOrder(UUID FileUUID, Long chunkOrder);

    List<ChunkEntity> deleteAllByFileUUID(UUID FileUUID);
}
