package com.tre3p.fileserver.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class PasswordEncryptorServiceTest {

    @Autowired
    private PasswordEncryptorService passwordEncryptorService;

    private final String randomPasswordToEncrypt = "testRNDpassword";

    @Test
    void testEncryptedAndOriginalPasswordNotMatches() throws
            NoSuchPaddingException,
            IllegalBlockSizeException,
            NoSuchAlgorithmException,
            BadPaddingException,
            InvalidKeyException
    {
        byte[] encryptedPassword = passwordEncryptorService.encrypt(randomPasswordToEncrypt);
        assertNotEquals(randomPasswordToEncrypt, new String(encryptedPassword));
    }

    @Test
    void testEncryptedPasswordMatchesOriginalWhenDecrypt() throws
            NoSuchPaddingException,
            IllegalBlockSizeException,
            NoSuchAlgorithmException,
            BadPaddingException,
            InvalidKeyException
    {
        byte[] encryptedPassword = passwordEncryptorService.encrypt(randomPasswordToEncrypt);
        String decryptedPassword = new String(passwordEncryptorService.decrypt(encryptedPassword));

        assertEquals(decryptedPassword, randomPasswordToEncrypt);
    }

    @Test
    void testPasswordCantBeDecryptedWithAnotherKey() throws
            NoSuchPaddingException,
            IllegalBlockSizeException,
            NoSuchAlgorithmException,
            BadPaddingException,
            InvalidKeyException {
        byte[] encryptedPassword = passwordEncryptorService.encrypt(randomPasswordToEncrypt);

        ReflectionTestUtils.setField(passwordEncryptorService, "encryptionKey", "gVkYp3s6v9y$B&E)");

        assertThrows(BadPaddingException.class, () -> {
            passwordEncryptorService.decrypt(encryptedPassword);
        });
    }
}
