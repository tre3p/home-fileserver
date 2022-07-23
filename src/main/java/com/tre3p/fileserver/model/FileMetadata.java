package com.tre3p.fileserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "original_size")
    private String originalSize;

    @NotNull
    @Column(name = "path")
    private String pathToFile;

    @NotNull
    @Column(name = "hash", unique = true)
    private String hash;

    @NotNull
    @Column(name = "password")
    @JsonIgnore
    private byte[] password;

    public FileMetadata(String originalFileName,
                        String contentType,
                        String originalSize,
                        String pathToFile,
                        String hash,
                        byte[] password) {
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.originalSize = originalSize;
        this.pathToFile = pathToFile;
        this.hash = hash;
        this.password = password;
    }
}
