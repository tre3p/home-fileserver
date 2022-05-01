package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.model.File;
import com.tre3p.fileserver.repository.FileRepository;
import com.tre3p.fileserver.service.FileCompressorService;
import com.tre3p.fileserver.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.zip.DataFormatException;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    private final FileCompressorService compressorService;

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
    public File compressAndSave(String fileName, String contentType, byte[] bytes) {
        return fileRepository.save(new File(
                fileName,
                contentType,
                compressorService.compress(bytes)
        ));
    }

    @Override
    public File decompressAndGetById(Integer id) throws DataFormatException {
        File dbFile = fileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        byte[] decompressedData = compressorService.decompress(dbFile.getData());
        dbFile.setData(decompressedData);

        return dbFile;
    }

}
