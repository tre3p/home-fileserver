package com.tre3p.fileserver.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.DataFormatException;

public interface FileCompressorService {
    void compress(Path source, String zippedName) throws IOException;
    byte[] decompress(byte[] data) throws DataFormatException;
}
