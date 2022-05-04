package com.tre3p.fileserver.service;

import com.tre3p.fileserver.service.impl.FileEncryptorServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
public class FileEncryptorServiceTest {

    private final FileEncryptorService encryptor = new FileEncryptorServiceImpl();

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(encryptor, "ALGO", "AES");
        ReflectionTestUtils.setField(encryptor, "encryptionKey", "vevnwecjc43cece[socmqweokcm34c[3");
    }

    @Test
    public void testOriginalBytesDoesNotMatchBytesAfterEncryption() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        byte[] testBytes = "SomeTestBodyHere".getBytes();
        byte[] encryptedBytes = encryptor.encrypt(testBytes);

        assertFalse(Arrays.equals(testBytes, encryptedBytes));
        assertTrue(testBytes.length < encryptedBytes.length);
    }

    @Test
    public void testOriginalBytesMatchesDecryptedBytes() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        byte[] testBytes = "SomeTestBodyHere".getBytes();
        byte[] encryptedBytes = encryptor.encrypt(testBytes);

        assertFalse(Arrays.equals(testBytes, encryptedBytes));

        byte[] decryptedBytes = encryptor.decrypt(encryptedBytes);

        assertTrue(Arrays.equals(testBytes, decryptedBytes));
    }
}
