package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.exception.IncorrectPasswordException;
import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.service.ArchiveService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
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
    public final File zipFile(String fileName, String sourceFile, String password) throws IOException {
        log.info("+zipFile(): sourceFile {}, fileName {}", sourceFile, fileName);

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        zipParameters.setDefaultFolderPath(DATASTORAGE);
        zipParameters.setCompressionMethod(CompressionMethod.DEFLATE);
        zipParameters.setFileNameInZip(fileName);

        ZipFile zipFile = new ZipFile(DATASTORAGE + fileName + ZIP, password.toCharArray());
        zipFile.addFile(sourceFile, zipParameters);

        log.info("-zipFile()");
        return zipFile.getFile();
    }

    @Override
    public final String unzipFile(FileMetadata fileMetadata, byte[] password) throws IncorrectPasswordException {
        log.info("+unzipFile(): unzipping file with id: {}", fileMetadata.getId());
        ZipFile zipFile = new ZipFile(fileMetadata.getPathToFile());
        zipFile.setPassword(new String(password).toCharArray());
        try {
            zipFile.extractAll(DATASTORAGE);
        } catch (ZipException e) {
            throw new IncorrectPasswordException("Password is incorrect.");
        }

        String filePath = new File(DATASTORAGE + fileMetadata.getOriginalFileName()).getAbsolutePath();
        log.info("unzipFile: filePath {}", filePath);
        return filePath;
    }
}
