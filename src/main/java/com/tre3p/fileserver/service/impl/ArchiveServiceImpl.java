package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.service.ArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class ArchiveServiceImpl implements ArchiveService {

    @Override
    public void zipFile(String fileName, String filePath, File file) throws IOException {

        log.debug("-zipFile() file path {}", file.getAbsolutePath());

        String sourceFile =  file.getAbsolutePath();
        FileOutputStream fos = new FileOutputStream("/datastorage/" + fileName + ".zip");
        ZipOutputStream zos = new ZipOutputStream(fos);
        File fileToZip = new File(sourceFile);
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;

        while((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.close();
        fis.close();
        fos.close();
    }
}
