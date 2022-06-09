package com.tre3p.fileserver.service;

import com.tre3p.fileserver.exception.IncorrectPasswordException;
import com.tre3p.fileserver.model.FileMetadata;
import net.lingala.zip4j.exception.ZipException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface ArchiveService {

    File zipFile(String fileName, String sourceFile, String password) throws
            IOException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            BadPaddingException,
            InvalidKeyException;
    String unzipFile(FileMetadata fileMetadata, byte[] password) throws
            ZipException,
            FileNotFoundException,
            IncorrectPasswordException;
}
