package com.tre3p.fileserver.service;

import com.tre3p.fileserver.service.impl.ArchiveServiceImpl;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileDeleteStrategy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArchiveServiceTest {

    private static ArchiveService archiveService;

    private static Path pathToDataStorage;

    @BeforeAll
    public static void init() throws IOException {
        archiveService = new ArchiveServiceImpl();

        pathToDataStorage = Paths.get("/application/datastorage").toAbsolutePath().normalize();
        if (!Files.exists(pathToDataStorage)) {
            Files.createDirectories(pathToDataStorage);
        }
    }

    @AfterAll
    public static void afterAll() throws IOException {
        FileDeleteStrategy.FORCE.delete(new File(String.valueOf(pathToDataStorage)));
    }

    @Test
    public void testZipFileIsProtectedAndValid() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        File file = archiveService.zipFile("tst_file.txt", "src/test/resources/tst_file.txt", "sssss");
        ZipFile zippedFile = new ZipFile(file);
        assertTrue(zippedFile.isEncrypted());
        assertTrue(zippedFile.isValidZipFile());
        assertTrue(file.getAbsolutePath().endsWith(".zip"));
        assertThrows(ZipException.class, () -> {
           zippedFile.extractAll("/tst");
        }, "empty or null password provided for AES decryption");
    }

    @Test
    public void testZipFileCanBeUnencryptedWithCorrectPassword() throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        File file = archiveService.zipFile("tst_file.txt", "src/test/resources/tst_file.txt", "sssss");
        ZipFile zipFile = new ZipFile(file);
        zipFile.setPassword("sssss".toCharArray());
        Assertions.assertDoesNotThrow(() -> {
            zipFile.extractAll("/tst");
        });
    }

    @Test
    public void testZipFileExtractingWillFailWhenPasswordIsWrong() throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        File file = archiveService.zipFile("tst_file.txt", "src/test/resources/tst_file.txt", "sssss");
        ZipFile zipFile = new ZipFile(file);
        zipFile.setPassword("ss123".toCharArray());
        Assertions.assertThrows(ZipException.class, () -> {
            zipFile.extractAll("/tst");
        }, "Wrong Password");
    }
}
