package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.service.ArchiveService;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.ZipParameters;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;

import static com.tre3p.fileserver.util.Constants.ZIP;
import static com.tre3p.fileserver.util.Constants.DATASTORAGE;
import static net.lingala.zip4j.model.enums.CompressionMethod.DEFLATE;
import static net.lingala.zip4j.model.enums.EncryptionMethod.AES;

@Slf4j
@Service
public class ArchiveServiceImpl implements ArchiveService {

    @Override
    public final File zipFile(String fileName, String sourceFilePath, String password) throws IOException {
        log.info("+zipFile(): sourceFile {}, fileName {}", sourceFilePath, fileName);

        ZipFile zipFile = new ZipFile(DATASTORAGE + fileName + ZIP, password.toCharArray());
        zipFile.addFile(sourceFilePath, getZipParameters(fileName));

        log.info("-zipFile()");
        return zipFile.getFile();
    }

    @Override
    public final ZipInputStream unzipFile(FileMetadata fileMetadata, byte[] password) throws IOException {
        log.info("+unzipFile(): unzipping file with id: {}", fileMetadata.getId());
        ZipFile zipFile = new ZipFile(fileMetadata.getPathToFile());
        zipFile.setPassword(new String(password).toCharArray());
        log.info("-unzipFile()");
        return zipFile.getInputStream(zipFile.getFileHeader(fileMetadata.getOriginalFileName()));
    }

    private ZipParameters getZipParameters(String fileName) {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(AES);
        zipParameters.setDefaultFolderPath(DATASTORAGE);
        zipParameters.setCompressionMethod(DEFLATE);
        zipParameters.setFileNameInZip(fileName);
        return zipParameters;
    }
}
