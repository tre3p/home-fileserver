package com.tre3p.fileserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@SpringBootApplication
public class FileServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(FileServerApplication.class, args);
    }

    @Override
    public final void run(String... args) throws Exception {
        try {
            Path path = Paths.get("/application/datastorage").toAbsolutePath().normalize();
            if (!Files.exists(path)) {
                log.info("Directory not exists, creating..");
                Files.createDirectories(path);
                log.info("Directory created.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot create directory");
        }
    }
}
