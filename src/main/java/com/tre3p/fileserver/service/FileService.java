package com.tre3p.fileserver.service;

import com.tre3p.fileserver.model.File;

public interface FileService {
    Iterable<File> getAll();
    File getById(Integer id);
    void removeById(Integer id);
    File save(String fileName, String contentType, byte[] bytes);
}
