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
    FileMetadata getById(Integer id) throws FileNotFoundException;
    void removeById(Integer id) throws FileNotFoundException;
    FileMetadata prepareAndSave(String fileName, String contentType, File file) throws IOException;
    //byte[] decompressAndGetById(Integer id) throws DataFormatException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
}
