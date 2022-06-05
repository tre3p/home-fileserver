package com.tre3p.fileserver.service;

import com.tre3p.fileserver.model.FileMetadata;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface FileService {
    List<FileMetadata> getAll();
    void removeById(Integer id) throws FileNotFoundException;
    FileMetadata prepareForSaving(String fileName, String contentType, File file) throws IOException;
    FileMetadata getById(Integer id);
    FileMetadata save(String fileName, String contentType, File file,
                      int formatNum, String originalSize, String zippedSize);
}
