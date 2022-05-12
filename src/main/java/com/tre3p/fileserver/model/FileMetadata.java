package com.tre3p.fileserver.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

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

    @NotEmpty
    private String fileName;

    private String contentType;

    private boolean isZipped;

    private String originalSize;

    private String zippedSize;

    @NotEmpty
    private String pathToFile;

    public FileMetadata(String fileName, String contentType, boolean isZipped, String originalSize, String zippedSize, String pathToFile) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.isZipped = isZipped;
        this.originalSize = originalSize;
        this.zippedSize = zippedSize;
        this.pathToFile = pathToFile;
    }
}
