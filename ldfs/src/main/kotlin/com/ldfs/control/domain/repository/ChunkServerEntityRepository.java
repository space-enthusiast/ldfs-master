package com.ldfs.control.domain.repository;

import com.ldfs.control.domain.model.entity.ChunkServerEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface ChunkServerEntityRepository extends JpaRepository<ChunkServerEntity, UUID> {
    ChunkServerEntity findByIp(String IpAddress);

    ChunkServerEntity findByIpAndPort(String IpAddress, String port);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT i FROM ChunkServerEntity i WHERE i.ip = ?1 AND i.port = ?2")
    ChunkServerEntity findByIpAndPortWithLock(String IpAddress, String port);

    @Query(value = "SELECT i FROM ChunkServerEntity i WHERE i.remainingStorageSize = (SELECT MAX(i.remainingStorageSize) FROM ChunkServerEntity i)")
    List<ChunkServerEntity> findByLargestRemainingStorageSize();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT i FROM ChunkServerEntity i WHERE i.remainingStorageSize = (SELECT MAX(i.remainingStorageSize) FROM ChunkServerEntity i)")
    List<ChunkServerEntity> findByLargestRemainingStorageSizeByLock();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT i FROM ChunkServerEntity i")
    List<ChunkServerEntity> findAllByLock();
}
