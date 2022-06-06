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

    private static final String DATASTORAGE = "/application/datastorage/";

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
    public final FileMetadata prepareForSaving(String fileName, String contentType, File file) throws IOException {
        log.info("+prepareAndSave(): filename: {}, contentType: {}", fileName, contentType);
        if (file.exists()) {
            log.info("prepareAndSave(): file exists, saving..");

            File newFile = moveFileToMainStorage(file, fileName);
            log.info("newFile path: {}", newFile.getAbsolutePath());

            File zippedFile = archiveService.zipFile(fileName, newFile.getAbsolutePath());
            log.info("file path after zipping: {}", zippedFile.getAbsolutePath());

            String originalSize = calculateSize(newFile.length());

            int numOfFormat = compareFileSizes(newFile.length(), zippedFile.length()); //сравниваем форматы,получаем 1/0
            File fileForSave = numOfFormat == 1 ? zippedFile : newFile; // получаем файл в зависимости от номера
            removeNotSelectedFormat(numOfFormat, newFile, zippedFile); // удаляем ненужный формат

            String zippedSize = calculateSize(zippedFile.length());

            log.info("-prepareAndSave(): file successfully saved at {}", fileForSave.getPath());
            return save(fileName, contentType, fileForSave, numOfFormat, originalSize, zippedSize);
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
                                   int numFormat,
                                   String originalSize,
                                   String zippedSize) {
        return fileRepository.save(new FileMetadata(
                fileName,
                file.getName(),
                contentType,
                archiveService.isZipped(numFormat),
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

    private void removeNotSelectedFormat(int numOfFormat, File originalFile, File zippedFile) {
        boolean isRemoved = numOfFormat == 1 ? originalFile.delete() : zippedFile.delete();
        log.info("-removeNotSelectedFormat() is file removed: {}", isRemoved);
    }

    private File moveFileToMainStorage(File file, String originalFileName) throws IOException {
        log.info("+moveFileToMainStorage(): moving to: {}", DATASTORAGE + originalFileName);
        return Files.move(Paths.get(
                file.getAbsolutePath()),
                Paths.get(DATASTORAGE + originalFileName)
        ).toFile();
    }
}
