package com.tre3p.fileserver.service;

import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.service.impl.ArchiveServiceImpl;
import com.tre3p.fileserver.utils.TestUtils;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArchiveServiceTest {

    private static ArchiveServiceImpl archiveService;
    private static File file1, file2, file3, file4;

    @Rule
    private static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @BeforeAll
    static void setUp() throws IOException {
        temporaryFolder.create();
        file1 = temporaryFolder.newFile("tempfile1.txt");
        file2 = temporaryFolder.newFile("tempfile2.txt");
        file3 = temporaryFolder.newFile("tempfile3.txt");
        file4 = temporaryFolder.newFile("tempfile4.txt");
        TestUtils.writeDemoDataToFile(file4, "test message");

        archiveService = new ArchiveServiceImpl();

        Path path = Paths.get("/application/datastorage").toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    @Test
    void testArchiveIsValid() throws IOException {
        File archivedFile = archiveService.zipFile(file1.getName(), file1.getAbsolutePath(), "testpassword");
        ZipFile zipFile = new ZipFile(archivedFile);
        assertTrue(zipFile.isValidZipFile());
    }

    @Test
    void testArchiveIsEncrypted() throws IOException {
        File encryptedFile = archiveService.zipFile(file2.getName(), file2.getAbsolutePath(), "testpassword");
        ZipFile zipFile = new ZipFile(encryptedFile);
        assertTrue(zipFile.isEncrypted());
        assertThrows(ZipException.class, () -> {
            zipFile.extractAll("/tmp");
        }, "empty or null password provided for AES decryption");
    }

    @Test
    void testFilesFromArchiveCantBeExtractedWithIncorrectPassword() throws IOException {
        File encryptedFile = archiveService.zipFile(file3.getName(), file3.getAbsolutePath(), "testpassword");
        ZipFile zipFile = new ZipFile(encryptedFile);
        zipFile.setPassword("anotheronepassword".toCharArray());

        assertThrows(ZipException.class, () -> {
            zipFile.extractAll("/tmp");
        }, "Wrong Password");
    }

/*    @Test
    void testFileFromArchiveCanBeExtractedWithCorrectPassword() throws IOException {
        archiveService.zipFile(file4.getName(), file4.getAbsolutePath(), "passwd");
        File decryptedFile = archiveService.unzipFile(
                new FileMetadata(
                        file4.getName(),
                        "application/txt",
                        "101kb",
                        file4.getAbsolutePath(),
                        "rndhash",
                        "passwd".getBytes()
                ),
                "passwd".getBytes()
        );

        String originalFileContent = Files.readString(Path.of(file4.getAbsolutePath()));
        String decryptedFileContent = Files.readString(Path.of(decryptedFile.getAbsolutePath()));

        assertEquals(originalFileContent, decryptedFileContent);
    }*/
}
