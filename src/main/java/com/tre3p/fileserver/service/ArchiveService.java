package com.tre3p.fileserver.service;

import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;

public interface ArchiveService {

    File zipFile(String fileName, String sourceFile) throws IOException;
    boolean isZipped(int numFormat);
    void unzipFile(String sourceZippedFile, String originalFileName) throws ZipException;
}
