package com.tre3p.fileserver.controller;

import com.tre3p.fileserver.model.File;
import com.tre3p.fileserver.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping
    public Iterable<File> getAllFiles() {
        return fileService.getAll();
    }

    @GetMapping("/{id}")
    public File getById(@PathVariable Integer id) {
        return fileService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        fileService.removeById(id);
    }

    @PostMapping
    public File uploadNewFile(@RequestPart("data") MultipartFile file) throws IOException {
        return fileService.save(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );
    }
}
