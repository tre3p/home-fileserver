package com.tre3p.fileserver.repository;

import com.tre3p.fileserver.model.FileMetadata;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Repository
public interface FileRepository extends JpaRepository<FileMetadata, Integer> {
    @Transactional
    default FileMetadata get(Integer id) {
        FileMetadata fm = this.getById(id);
        long start = System.currentTimeMillis();
        Hibernate.initialize(fm.getFileContent());
        long elapsed = System.currentTimeMillis() - start;
        DateFormat df = new SimpleDateFormat("HH 'hours', mm 'mins,' ss 'seconds'");
        df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        System.out.println(df.format(new Date(elapsed)));
        return fm;
    }
}
