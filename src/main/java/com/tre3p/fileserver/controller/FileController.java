package com.tre3p.fileserver.controller;

import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/files")
public class FileController {

    private FileService fileService;

    @GetMapping
    public List<FileMetadata> getAllFiles() {
        return fileService.getAll();
    }

    @PostMapping
    @CrossOrigin("http://localhost:3000")
    public void uploadFile(@RequestPart("data") MultipartFile file) throws IOException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            NoSuchAlgorithmException,
            BadPaddingException,
            InvalidKeyException {
        File file1 = new File("tmp");
        FileOutputStream fos = new FileOutputStream(file1);
        fos.write(file.getBytes());
        fos.close();
        fileService.prepareAndSave(file.getOriginalFilename(), file.getContentType(), file1);
    }

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/{id}")
    public String getHashById(@PathVariable(name = "id") Integer id) {
        return fileService.getById(id).getHash();
    }

    @CrossOrigin("http://localhost:3000")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Integer id) throws FileNotFoundException {
        fileService.removeById(id);
    }
}
