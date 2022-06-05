package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.repository.FileRepository;
import com.tre3p.fileserver.service.ArchiveService;
import com.tre3p.fileserver.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final ArchiveService archiveService;

    private static final String DATASTORAGE = "/datastorage/";

    @Override
    public List<FileMetadata> getAll() {
        return fileRepository.findAll();
    }

    @Override
    public void removeById(Integer id) throws FileNotFoundException {
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
    public FileMetadata prepareForSaving(String fileName, String contentType, File file) throws IOException {
        log.info("+prepareAndSave(): filename: {}, contentType: {}", fileName, contentType);
        if (file.exists()) {
            log.info("prepareAndSave(): file exists, saving..");

            File newFile = moveFileToMainStorage(file, fileName);

            File zippedFile = archiveService.zipFile(fileName, newFile.getAbsolutePath());

            String originalSize = calculateSize(newFile.length());

            int fileFormatNum = compareFileSizes(newFile.length(), zippedFile.length());
            File fileForSave = fileFormatNum == 1 ? zippedFile : newFile;
            deleteFileFormat(fileFormatNum, newFile, zippedFile);
            String zippedSize = calculateSize(zippedFile.length());

            log.info("-prepareAndSave(): file successfully saved at {}", fileForSave.getPath());
            return save(fileName, contentType, fileForSave, fileFormatNum, originalSize, zippedSize);
        } else {
            log.error("-prepareAndSave(): file not exists");
            throw new FileNotFoundException("File not exists");
        }
    }

    @Override
    public FileMetadata getById(Integer id) {
        return fileRepository.getById(id);
    }

    @Override
    public FileMetadata save(String fileName, String contentType, File file,
                             int formatNum, String originalSize, String zippedSize) {
        return fileRepository.save(new FileMetadata(
                fileName,
                file.getName(),
                contentType,
                isZipped(formatNum),
                originalSize,
                zippedSize,
                file.getAbsolutePath()
        ));
    }


    private String calculateSize(long length) {
        String result = FileUtils.byteCountToDisplaySize(length);
        return result.equals("0 bytes") ? "-" : result; // todo: костыль, надо переделать
    }

    private int compareFileSizes(long originalSize, long zippedSize) {
        log.info("+compareFileSizes(): originalSize: {}. zippedSize: {}", originalSize, zippedSize);
        int result =  zippedSize > originalSize ? 0 : 1;
        log.info("compareFileSizes(): result {}", result);
        return result;
    }

    private void deleteFileFormat(int fileFormatNum, File originalFile, File zippedFile) { // todo: сделать красивей
        if (fileFormatNum == 1) {
            log.info("deleteFileFormat: deleting original file {} ...", originalFile);
            originalFile.delete();
            return;
        }
        log.info("deleteFileFormat: deleting zipped file {} ...", zippedFile);
        zippedFile.delete();
    }

    private boolean isZipped(int formatNum) {
        return formatNum == 1 ? true : false; // todo: перенести метод в зип сервис
    }


    private File moveFileToMainStorage(File file, String originalFileName) throws IOException {
        return Files.move(Paths.get(
                file.getAbsolutePath()),
                Paths.get(DATASTORAGE + originalFileName)
        ).toFile();
    }
}
