package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.repository.FileRepository;
import com.tre3p.fileserver.service.ArchiveService;
import com.tre3p.fileserver.service.FileService;
import com.tre3p.fileserver.service.PasswordEncryptorService;
import com.tre3p.fileserver.util.RandomUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.tre3p.fileserver.util.Constants.DATASTORAGE;

@Slf4j
@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final ArchiveService archiveService;
    private final PasswordEncryptorService encryptorService;
    private final RandomUtils randomUtils;

    @Override
    public final List<FileMetadata> getAll() {
        return fileRepository.findAll();
    }

    @Override
    public final void removeById(Integer id) throws FileNotFoundException {
        log.info("+removeById(): id: {}", id);
        FileMetadata savedFile = fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not exists"));
        log.info("removeById(): file found, deleting..");

        File file = new File(savedFile.getPathToFile());
        if (file.delete()) {
            log.info("removeById(): file deleted from storage, deleting from database...");
            fileRepository.deleteById(id);
            log.info("removeById(): file deleted from database");
        }
        log.info("-removeById(): file with ID {} successfully deleted", id);
    }

    @Override
    public final void prepareAndSave(String fileName, String contentType, File file) throws IOException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            BadPaddingException,
            InvalidKeyException {
        log.info("+prepareAndSave(): filename: {}, contentType: {}", fileName, contentType);
        if (file.exists()) {
            log.info("prepareAndSave(): file exists, saving..");

            File newFile = moveFileToMainStorage(file, fileName);
            log.info("prepareAndSave(): newFile path: {}", newFile.getAbsolutePath());
            String fileSize = calculateSize(newFile.length());

            String randomPassword = randomUtils.generateRandomPassword();
            byte[] encryptedPassword = encryptorService.encrypt(randomPassword);
            String randomHash = randomUtils.generateRandomAlphaNumericHash();

            File zippedFile = archiveService.zipFile(fileName, newFile.getAbsolutePath(), randomPassword);
            log.info("prepareAndSave(): file path after zipping: {}", zippedFile.getAbsolutePath());
            newFile.delete();

            log.info("-prepareAndSave(): file successfully saved at {}", zippedFile.getPath());
            save(fileName, contentType, zippedFile, fileSize, randomHash, encryptedPassword);
        } else {
            log.error("-prepareAndSave(): file not exists");
            throw new FileNotFoundException("File not exists");
        }
    }

    @Override
    public final FileMetadata getById(Integer id) {
        return fileRepository.getById(id);
    }

    @Override
    public final FileMetadata save(String fileName,
                                   String contentType,
                                   File file,
                                   String originalSize,
                                   String hash,
                                   byte[] password) {
        log.info("save()");
        return fileRepository.save(new FileMetadata(
                fileName,
                contentType,
                originalSize,
                file.getAbsolutePath(),
                hash,
                password
        ));
    }

    @Override
    public final FileInputStream prepareForDownload(Integer id) throws NoSuchPaddingException,
            IllegalBlockSizeException,
            NoSuchAlgorithmException,
            BadPaddingException,
            InvalidKeyException,
            IOException {
        log.info("+prepareForDownload(): id: {}", id);
        FileMetadata zippedFile = getById(id);
        byte[] password = encryptorService.decrypt(zippedFile.getPassword());
        File resultFile = archiveService.unzipFile(zippedFile, password);
        log.info("-prepareForDownload()");
        return new FileInputStream(resultFile);
    }


    private String calculateSize(long length) {
        log.info("calculateSize()");
        return FileUtils.byteCountToDisplaySize(length);
    }

    private File moveFileToMainStorage(File file, String originalFileName) throws IOException {
        log.info("moveFileToMainStorage(): moving to: {}", DATASTORAGE + originalFileName);
        return Files.move(Paths.get(
                        file.getAbsolutePath()),
                Paths.get(DATASTORAGE + originalFileName)
        ).toFile();
    }

    @Override
    public final String buildPathToFileHash(String randomHash) {
        return ServletUriComponentsBuilder.fromCurrentServletMapping().toUriString() + "/file/" + randomHash;
    }
}
