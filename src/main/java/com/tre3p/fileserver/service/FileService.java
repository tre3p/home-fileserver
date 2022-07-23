package com.tre3p.fileserver.service;

import com.tre3p.fileserver.model.FileMetadata;
import net.lingala.zip4j.io.inputstream.ZipInputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface FileService {
    List<FileMetadata> getAll();
    void removeById(Integer id) throws FileNotFoundException;
    void prepareAndSave(String fileName, String contentType, File file) throws
            IOException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            BadPaddingException,
            InvalidKeyException;
    FileInputStream prepareForDownload(Integer id) throws
            NoSuchPaddingException,
            IllegalBlockSizeException,
            NoSuchAlgorithmException,
            BadPaddingException,
            InvalidKeyException,
            IOException;
    FileMetadata getById(Integer id);
    FileMetadata save(String fileName,
                      String contentType,
                      File file,
                      String originalSize,
                      String hash,
                      byte[] password);
    String buildPathToFileHash(String hash);
}
