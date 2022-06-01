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

    @Override
    public final File zipFile(String fileName, String sourceFile) throws IOException {
        log.info("+zipFile(): sourceFile {}, fileName {}", sourceFile, fileName);

        File fileToZip = new File(sourceFile);
        FileInputStream inputStream = new FileInputStream(fileToZip);

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setDefaultFolderPath(DATASTORAGE);
        zipParameters.setCompressionMethod(CompressionMethod.DEFLATE);
        zipParameters.setFileNameInZip(fileName);

        String zipFileName = fileName + ".zip";
        ZipFile zipFile = new ZipFile(zipFileName);
        zipFile.addStream(inputStream, zipParameters);

        zipFile.renameFile(zipFileName, DATASTORAGE + zipFileName);

        inputStream.close();

        log.info("-zipFile()");
        return zipFile.getFile();
    }

    //todo: сделать метод для анзипа, и в сущность добавлять признак архивированности isZipped.
    // todo: если файл зипованый - делаем анзип, если нет - отдаем как есть ПРИ СКАЧИВАНИИ
}
