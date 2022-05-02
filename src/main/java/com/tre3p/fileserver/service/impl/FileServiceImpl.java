package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.model.FileContent;
import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.repository.FileRepository;
import com.tre3p.fileserver.service.FileCompressorService;
import com.tre3p.fileserver.service.FileEncryptorService;
import com.tre3p.fileserver.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.zip.DataFormatException;

@Slf4j
@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    private final FileCompressorService compressorService;

    private final FileEncryptorService encryptorService;

    @Override
    public Iterable<FileMetadata> getAll() {
        return fileRepository.findAll();
    }

    @Override
    public FileMetadata getById(Integer id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public void removeById(Integer id) {
        fileRepository.deleteById(id);
    }

    @Override
    public CompletableFuture<FileMetadata> prepareAndSaveAsync(String fileName, String contentType, byte[] bytes) {
        return CompletableFuture.supplyAsync(() -> {
            byte[] zippedData = compressorService.compress(bytes);
            byte[] encryptedData;

            if (zippedData.length > bytes.length) {
                try {
                    encryptedData = encryptorService.encrypt(bytes);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                return fileRepository.save(new FileMetadata(
                        fileName,
                        contentType,
                        new FileContent(encryptedData),
                        false
                ));
            }

            try {
                encryptedData = encryptorService.encrypt(zippedData);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return fileRepository.save(new FileMetadata(
                    fileName,
                    contentType,
                    new FileContent(encryptedData),
                    true
            ));
        });
    }


    /**
     *
     * @param fileName
     * @param contentType
     * @param bytes
     * @return
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     *
     * Implementation without async call from controller. For now, application uses prepareAndSaveAsync method.
     *
     */
    @Override
    public FileMetadata prepareAndSave(String fileName, String contentType, byte[] bytes) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        log.info("+prepareAndSave(): fileName: {}, contentType: {}, size: {}", fileName, contentType, bytes.length);
        byte[] zippedData = compressorService.compress(bytes);
        byte[] encryptedData;

        if (zippedData.length > bytes.length) {
            log.info("prepareAndSave(): zipped data is bigger then original. Setting original data as default");
            encryptedData = encryptorService.encrypt(bytes); // todo: refactoring

            log.info("-prepareAndSave()");
            return fileRepository.save(new FileMetadata(
                    fileName,
                    contentType,
                    new FileContent(encryptedData),
                    false
            ));
        }

        encryptedData = encryptorService.encrypt(zippedData);

        log.info("-prepareAndSave()");
        return fileRepository.save(new FileMetadata(
                fileName,
                contentType,
                new FileContent(encryptedData),
                true
        ));
    }

    @Override
    public FileMetadata decompressAndGetById(Integer id) throws DataFormatException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        log.info("+decompressAndGetById(): id: {}", id);
        FileMetadata dbFile = fileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        byte[] originalData = dbFile.getFileContent().getContent();
        byte[] decryptedData;
        try {
            decryptedData = encryptorService.decrypt(originalData);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        if (dbFile.isZipped()) {
            log.info("decompressAndGetById(): file is zipped. decompressing..");
            byte[] decompressedData = compressorService.decompress(decryptedData); // todo: refactoring
            dbFile.setFileContent(new FileContent(decompressedData));
            return dbFile;
        }

        log.info("+decompressAndGetById(): file is not zipped.");
        dbFile.setFileContent(new FileContent(decryptedData));

        return dbFile;
    }

}
