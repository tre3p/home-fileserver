package com.tre3p.fileserver.service;

import com.tre3p.fileserver.model.File;

import java.util.zip.DataFormatException;

public interface FileService {
    Iterable<File> getAll();
    File getById(Integer id);
    void removeById(Integer id);
    File compressAndSave(String fileName, String contentType, byte[] bytes);

    File decompressAndGetById(Integer id) throws DataFormatException;
}
