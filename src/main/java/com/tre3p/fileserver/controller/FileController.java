package com.tre3p.fileserver.controller;

import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping
    public Iterable<FileMetadata> getAllFiles() {
        log.info("getAllFiles()");
        return fileService.getAll();
    }

    @GetMapping("/{id}")
    public FileMetadata getById(@PathVariable Integer id) {
        log.info("getById(): getting file with id: {}", id);
        return fileService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        log.info("delete(): deleting file with id: {}", id);
        fileService.removeById(id);
    }

    @PostMapping
    public FileMetadata uploadNewFile(@RequestPart("data") MultipartFile file) throws IOException {
        log.info("uploadNewFile(): file with name \"{}\" and size {} is uploading", file.getOriginalFilename(), file.getSize());
        return fileService.compressAndSave(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );
    }
}
