package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.model.File;
import com.tre3p.fileserver.repository.FileRepository;
import com.tre3p.fileserver.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public Iterable<File> getAll() {
        return fileRepository.findAll();
    }

    @Override
    public File getById(Integer id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public void removeById(Integer id) {
        fileRepository.deleteById(id);
    }

    @Override
    public File save(String fileName, String contentType, byte[] bytes) {
        return fileRepository.save(new File(
                fileName,
                contentType,
                bytes
        ));
    }
}
