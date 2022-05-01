package com.tre3p.fileserver.dto;

import lombok.Data;


@Data
public class FileDisplayDto {
    private Integer id;
    private String fileName;
    private String contentType;
}
