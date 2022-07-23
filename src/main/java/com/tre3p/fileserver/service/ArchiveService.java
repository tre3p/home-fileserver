package com.tre3p.fileserver.service;

import com.tre3p.fileserver.model.FileMetadata;
import net.lingala.zip4j.io.inputstream.ZipInputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
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
    File unzipFile(FileMetadata fileMetadata, byte[] password) throws
            IOException;
}
