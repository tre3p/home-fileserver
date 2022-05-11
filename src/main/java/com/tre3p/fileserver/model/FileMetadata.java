package com.tre3p.fileserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.File;

@Entity
@Getter
@Setter
@Table(name = "file_metadata")
@NoArgsConstructor
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

    private File pathToFile;

    public FileMetadata(String fileName, String contentType, boolean isZipped, String originalSize, String zippedSize, File pathToFile) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.isZipped = isZipped;
        this.originalSize = originalSize;
        this.zippedSize = zippedSize;
        this.pathToFile = pathToFile;
    }
}
