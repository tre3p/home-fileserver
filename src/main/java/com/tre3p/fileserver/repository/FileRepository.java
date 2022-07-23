package com.tre3p.fileserver.repository;

import com.tre3p.fileserver.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileMetadata, Integer> {
    @Query("SELECT f FROM FileMetadata f WHERE f.hash = ?1")
    Optional<FileMetadata> findByHash(String hash);
}
