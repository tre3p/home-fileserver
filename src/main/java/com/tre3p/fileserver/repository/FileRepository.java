package com.tre3p.fileserver.repository;

import com.tre3p.fileserver.model.FileMetadata;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface FileRepository extends JpaRepository<FileMetadata, Integer> {
    @Transactional
    default FileMetadata get(Integer id) {
        FileMetadata fm = this.getById(id);
        Hibernate.initialize(fm.getFileContent());
        return fm;
    }
}
