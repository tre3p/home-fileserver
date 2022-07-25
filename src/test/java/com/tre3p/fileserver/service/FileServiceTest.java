package com.tre3p.fileserver.service;

import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.repository.FileRepository;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class FileServiceTest {

    @Autowired
    private FileService fileService;

    @MockBean
    private FileRepository fileRepository;

    private static FileMetadata mockFileMetadata;

    private static File mockFile;

    @Rule
    private static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @BeforeAll
    static void setUp() throws IOException {
        temporaryFolder.create();

        mockFile = temporaryFolder.newFile("testfile.txt");

        mockFileMetadata = new FileMetadata(
                "testfile.txt",
                "application/txt",
                "1kb",
                mockFile.getAbsolutePath(),
                "randomhash",
                "testpswd".getBytes()
        );
    }

    @Test
    void testFileRepositoryCallsGetAllWhenItsCalledFromService() {
        fileService.getAll();
        Mockito.verify(fileRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testFileRepositoryThrowsExceptionOnNonExistId() {
        assertThrows(FileNotFoundException.class, () -> {
            fileService.removeById(2);
        });
    }

    @Test
    void testFileServiceCreatesCorrectFiles() throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        File zippedFile = fileService.prepareAndSave(
                mockFileMetadata.getOriginalFileName(),
                mockFileMetadata.getContentType(),
                mockFile
        );

        ZipFile encryptedFile = new ZipFile(zippedFile);

        Assertions.assertTrue(zippedFile.getAbsolutePath().contains("datastorage"));
        Assertions.assertTrue(encryptedFile.isEncrypted());
        Assertions.assertTrue(encryptedFile.isValidZipFile());
        assertThrows(ZipException.class, () -> {
            encryptedFile.extractAll("/tmp");
        }, "empty or null password provided for AES decryption");
    }

    @Test
    void testFileServiceCallsCorrectRepoMethod() {
        fileService.getById(1);
        Mockito.verify(fileRepository).getById(Mockito.anyInt());
    }
}
