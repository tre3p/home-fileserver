package com.tre3p.fileserver.controller;

import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.repository.FileRepository;
import com.tre3p.fileserver.service.FileService;
import lombok.AllArgsConstructor;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@AllArgsConstructor
@RestController
@RequestMapping("/file")
public class FileURLDownloadController {

    private FileService fileService;
    private FileRepository repository;

    /**
     *
     * @param hash - hash of file which gonna be downloaded
     * @return InputStreamResource of file with specific hash
     * @throws Exception
     */
    @GetMapping("/{hash}")
    public ResponseEntity<InputStreamResource> dwnld(@PathVariable("hash") String hash) throws Exception {
        FileMetadata dbMetadata = repository.findByHash(hash)
                .orElseThrow(FileNotFoundException::new);

        ZipInputStream zipInputStream = fileService.prepareForDownload(dbMetadata.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(dbMetadata.getContentType()));
        headers.setContentDispositionFormData("attachment", dbMetadata.getOriginalFileName());

        InputStreamResource isr = new InputStreamResource(zipInputStream);

        return new ResponseEntity<>(isr, headers, HttpStatus.OK);
    }
}
