package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.service.ArchiveService;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionMethod;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Service
public class ArchiveServiceImpl implements ArchiveService {

    private static final String DATASTORAGE = "/datastorage/";
    private static String zipFileName;

    @Override
    public ZipFile zipFile(String fileName, String sourceFile) throws IOException {

        log.info("-zipFile() file path {}", sourceFile);

        File fileToZip = new File(sourceFile);
        FileInputStream inputStream = new FileInputStream(fileToZip);

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setDefaultFolderPath(DATASTORAGE);
        zipParameters.setCompressionMethod(CompressionMethod.DEFLATE);
        zipParameters.setFileNameInZip(fileName);

        zipFileName = fileName + ".zip";
        ZipFile zipFile = new ZipFile(zipFileName);
        zipFile.addStream(inputStream, zipParameters);

        zipFile.renameFile(zipFileName, DATASTORAGE + zipFileName);

        inputStream.close();

        return zipFile;
    }
}
