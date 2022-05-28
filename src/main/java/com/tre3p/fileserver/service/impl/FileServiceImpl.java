package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.repository.FileRepository;
import com.tre3p.fileserver.service.ArchiveService;
import com.tre3p.fileserver.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
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
    public FileMetadata prepareAndSave(String fileName, String contentType, File file) throws IOException {
        log.info("+prepareAndSave(): filename: {}, contentType: {}", fileName, contentType);
        if (file.exists()) {
            log.info("prepareAndSave(): file exists, saving..");

            File newFile = Files.move(Paths.get(
                    file.getAbsolutePath()),
                    Paths.get("/datastorage/" + fileName)
            ).toFile();

            ZipFile zippedFile = archiveService.zipFile(fileName, newFile.getAbsolutePath());

            newFile.delete();

            /*
            Сделать проверку на размер файла после сжатия.
            Если сжатый файл > чем оригинальный - сохраняем оригинальный файл.
            Если нет - сжатый.
             */

            long beforeCompress = zippedFile.getFile().length(); // file.length();

            log.info("-prepareAndSave(): file successfully saved at {}", newFile.getPath());
            return fileRepository.save(new FileMetadata(
                    fileName,
                    zippedFile.getFile().getName(), // file.getName()
                    contentType,
                    false,
                    calculateSize(beforeCompress),
                    calculateSize(beforeCompress),
                    zippedFile.getFile().getAbsolutePath() // file.getAbsolutePath()
                ));
        } else {
            log.error("-prepareAndSave(): file not exists");
            throw new FileNotFoundException("File not exists");
        }
    }

    @Override
    public FileMetadata getById(Integer id) {
        return fileRepository.getById(id);
    }

    private String calculateSize(long length) {
        return FileUtils.byteCountToDisplaySize(length);
    }

}
