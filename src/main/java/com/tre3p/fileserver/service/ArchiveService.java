package com.tre3p.fileserver.service;

import com.tre3p.fileserver.model.FileMetadata;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ArchiveService {

    File zipFile(String fileName, String sourceFile) throws IOException;
    boolean isZipped(int numFormat);
    String unzipFile(FileMetadata fileMetadata) throws ZipException, FileNotFoundException;
}
