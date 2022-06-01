package com.tre3p.fileserver.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "file_metadata")
@NoArgsConstructor
@Proxy(lazy = false)
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "original_file_name")
    private String originalFileName;

    @NotNull
    @Column(name = "zipped_file_name")
    private String zippedFileName;

    @Column(name = "content_type")
    private String contentType;

    @NotNull
    @Column(name = "is_zipped")
    private boolean isZipped;

    @Column(name = "original_size")
    private String originalSize;

    @Column(name = "zipped_size")
    private String zippedSize;

    @NotNull
    @Column(name = "path")
    private String pathToFile;

    public FileMetadata(String originalFileName,
                        String zippedFileName,
                        String contentType,
                        boolean isZipped,
                        String originalSize,
                        String zippedSize,
                        String pathToFile) {
        this.originalFileName = originalFileName;
        this.zippedFileName = zippedFileName;
        this.contentType = contentType;
        this.isZipped = isZipped;
        this.originalSize = originalSize;
        this.zippedSize = zippedSize;
        this.pathToFile = pathToFile;
    }
}
