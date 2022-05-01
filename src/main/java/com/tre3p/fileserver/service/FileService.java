package com.tre3p.fileserver.service;

import com.tre3p.fileserver.model.FileMetadata;

import java.util.zip.DataFormatException;

public interface FileService {
    Iterable<FileMetadata> getAll();
    FileMetadata getById(Integer id);
    void removeById(Integer id);
    FileMetadata compressAndSave(String fileName, String contentType, byte[] bytes);

    FileMetadata decompressAndGetById(Integer id) throws DataFormatException;
}
