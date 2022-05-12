package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.service.FileCompressorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.DataFormatException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class FileCompressorServiceImpl implements FileCompressorService {

    @Override
    public void compress(Path source, String zippedName) throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream("/datastorage/" + zippedName));
        FileInputStream fs = new FileInputStream(source.toFile());

        ZipEntry zipEntry = new ZipEntry(source.getFileName().toString());
        zipOutputStream.putNextEntry(zipEntry);

        byte[] buffer = new byte[1024];
        int len;
        while ((len = fs.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, len);
        }
        zipOutputStream.closeEntry();
    }

    @Override
    public byte[] decompress(byte[] data) throws DataFormatException {
        return new byte[0];
    }
}
