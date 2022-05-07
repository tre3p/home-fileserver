package com.tre3p.fileserver.service;

import com.tre3p.fileserver.model.FileMetadata;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.zip.DataFormatException;

public interface FileService {
    List<FileMetadata> getAll();
    void removeById(Integer id);
    FileMetadata prepareAndSave(String fileName, String contentType, byte[] bytes) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
    byte[] decompressAndGetById(Integer id) throws DataFormatException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
}
