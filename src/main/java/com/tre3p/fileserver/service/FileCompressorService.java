package com.tre3p.fileserver.service;

import java.util.zip.DataFormatException;

public interface FileCompressorService {
    byte[] compress(byte[] data);
    byte[] decompress(byte[] data) throws DataFormatException;
}
