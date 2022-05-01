package com.tre3p.fileserver.controller;

import com.tre3p.fileserver.model.File;
import com.tre3p.fileserver.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.zip.DataFormatException;

@AllArgsConstructor
@RestController
@RequestMapping("/download")
public class DownloadController {

    private final FileService fileService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Integer id) throws DataFormatException {
        File dbFile = fileService.decompressAndGetById(id);
        byte[] fileData = dbFile.getData();

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(dbFile.getFileName())
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf(dbFile.getContentType()));
        httpHeaders.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(fileData, httpHeaders, HttpStatus.OK);
    }
}
