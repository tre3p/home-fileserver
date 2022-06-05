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

            File zippedFile = archiveService.zipFile(fileName, newFile.getAbsolutePath());

            String originalSize = calculateSize(newFile.length());

            File fileForSave = compareFileSizes(newFile, zippedFile);
            String zippedSize = calculateSize(zippedFile.length());

            log.info("-prepareAndSave(): file successfully saved at {}", fileForSave.getPath());
            return save(fileName, contentType, fileForSave, originalSize, zippedSize);
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
                                   String zippedSize) {
        return fileRepository.save(new FileMetadata(
                fileName,
                file.getName(),
                contentType,
                false,
                originalSize,
                zippedSize,
                file.getAbsolutePath()
        ));
    }


    private String calculateSize(long length) {
        String result = FileUtils.byteCountToDisplaySize(length);
        return result.equals("0 bytes") ? "-" : result; // todo: костыль, надо переделать
    }

    private File compareFileSizes(File nativeFile, File zippedFile) {
        long originalSize = nativeFile.length();
        long zippedSize = zippedFile.length();
        log.info("compareFileSizes(): originalSize: {}. zippedSize: {}", originalSize, zippedSize);
        if (zippedSize > originalSize) {
            zippedFile.delete(); // todo: костыль поправить надо. отсюда убрать удаление файла и возвращать 0/1
            log.info("-compareFileSizes(): file saved as native format file");
            return nativeFile;
        }
        nativeFile.delete();
        log.info("-compareFileSizes(): file saved as zipped format file");
        return zippedFile;
    }

    private File moveFileToMainStorage(File file, String originalFileName) throws IOException {
        return Files.move(Paths.get(
                file.getAbsolutePath()),
                Paths.get(DATASTORAGE + originalFileName)
        ).toFile();
    }
}
