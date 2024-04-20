package com.ldfs.control.domain.repository;

import com.ldfs.control.domain.model.entity.Chunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChunkEntityRepository extends JpaRepository<Chunk, Long> {

    List<Chunk> findAllByFileId(Long fileId);

    List<Chunk> deleteAllByFileId(Long fileId);
}
