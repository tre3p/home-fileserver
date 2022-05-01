package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.model.FileContent;
import com.tre3p.fileserver.model.FileMetadata;
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
    public Iterable<FileMetadata> getAll() {
        return fileRepository.findAll();
    }

    @Override
    public FileMetadata getById(Integer id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public void removeById(Integer id) {
        fileRepository.deleteById(id);
    }

    @Override
    public FileMetadata compressAndSave(String fileName, String contentType, byte[] bytes) {
        return fileRepository.save(new FileMetadata(
                fileName,
                contentType,
                new FileContent(compressorService.compress(bytes))
        ));
    }

    @Override
    public FileMetadata decompressAndGetById(Integer id) throws DataFormatException {
        FileMetadata dbFile = fileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        byte[] decompressedData = compressorService.decompress(dbFile.getFileContent().getContent());
        dbFile.setFileContent(new FileContent(decompressedData));

        return dbFile;
    }

}
