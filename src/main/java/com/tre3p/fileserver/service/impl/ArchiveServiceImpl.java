package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.service.ArchiveService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionMethod;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class ArchiveServiceImpl implements ArchiveService {

    private static final String DATASTORAGE = "/application/datastorage/"; // todo: константа повторяется, надо вынести
    private static final String ZIP = ".zip";

    @Override
    public final File zipFile(String fileName, String sourceFile) throws IOException {
        log.info("+zipFile(): sourceFile {}, fileName {}", sourceFile, fileName);

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setDefaultFolderPath(DATASTORAGE);
        zipParameters.setCompressionMethod(CompressionMethod.DEFLATE);
        zipParameters.setFileNameInZip(fileName);

        ZipFile zipFile = new ZipFile(DATASTORAGE + fileName + ZIP);
        zipFile.addFile(sourceFile, zipParameters);

        log.info("-zipFile()");
        return zipFile.getFile();
    }

    @Override
    public final void unzipFile(String sourceZippedFile, String originalFileName) throws ZipException {
        new ZipFile(sourceZippedFile).extractFile(originalFileName, DATASTORAGE); // todo: найти,где вызывать этот метод
    }


    @Override
    public final boolean isZipped(int numFormat) {
        return numFormat == 1;
    }

    //todo: сделать метод для анзипа, и в сущность добавлять признак архивированности isZipped.
    // todo: если файл зипованый - делаем анзип, если нет - отдаем как есть ПРИ СКАЧИВАНИИ
}
