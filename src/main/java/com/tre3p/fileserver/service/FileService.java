package com.tre3p.fileserver.service;

import com.tre3p.fileserver.model.FileMetadata;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.zip.DataFormatException;

public interface FileService {
    Iterable<FileMetadata> getAll();
    FileMetadata getById(Integer id);
    void removeById(Integer id);

    CompletableFuture<FileMetadata> prepareAndSaveAsync(String fileName, String contentType, byte[] bytes);

    FileMetadata prepareAndSave(String fileName, String contentType, byte[] bytes) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

    FileMetadata decompressAndGetById(Integer id) throws DataFormatException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;


}
