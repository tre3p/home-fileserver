package com.tre3p.fileserver.controller;

import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/download")
public class DownloadController {
    private final FileService fileService;
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Integer id) throws DataFormatException {
        log.info("+download(): downloading file with id: {}", id);
        FileMetadata dbFile = fileService.decompressAndGetById(id);
        byte[] fileContent = dbFile.getFileContent().getContent();

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(dbFile.getFileName())
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf(dbFile.getContentType()));
        httpHeaders.setContentDisposition(contentDisposition);

        log.info("-download(): file with id {} was downloaded", id);
        return new ResponseEntity<>(fileContent, httpHeaders, HttpStatus.OK);
    }
}
