package com.tre3p.fileserver.service;

import com.tre3p.fileserver.model.FileMetadata;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.zip.DataFormatException;

public interface FileService {
    List<FileMetadata> getAll();
    void removeById(Integer id) throws FileNotFoundException;
    FileMetadata prepareForSaving(String fileName, String contentType, File file) throws IOException;
    FileMetadata getById(Integer id);
    FileMetadata save(String fileName, String contentType, File file,
                      int formatNum, String originalSize, String zippedSize);
}
