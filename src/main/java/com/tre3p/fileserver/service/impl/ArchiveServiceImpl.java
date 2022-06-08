package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.model.FileMetadata;
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
    public final String unzipFile(FileMetadata fileMetadata) throws ZipException {
        if (!fileMetadata.isZipped()) {
            log.info("unzipFile: in if return {}", fileMetadata.getPathToFile());
            return fileMetadata.getPathToFile();
        }
        ZipFile zipFile = new ZipFile(fileMetadata.getPathToFile());
        zipFile.extractAll(DATASTORAGE);

        String filePath = new File(DATASTORAGE + fileMetadata.getOriginalFileName()).getAbsolutePath();
        log.info("unzipFile: return {}", filePath);
        return filePath;
    }


    @Override
    public final boolean isZipped(int numFormat) {
        return numFormat == 1;
    }
}
