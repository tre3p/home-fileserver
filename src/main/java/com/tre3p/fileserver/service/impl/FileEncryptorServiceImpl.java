package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.service.FileEncryptorService;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
public class FileEncryptorServiceImpl implements FileEncryptorService {

    @Value("${security.encryption-key}")
    private String encryptionKey;

    @Value("${security.algorithm}")
    private String ALGO;

    @Override
    public byte[] encrypt(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        log.info("+encrypt(): encrypting file with size: {}", data.length);
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, generateKey(encryptionKey, ALGO));
        byte[] encryptedData = cipher.doFinal(data);
        log.info("-encrypt(): encryption done. total size: {}", encryptedData.length);
        return encryptedData;
    }

    @Override
    public byte[] decrypt(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        log.info("+decrypt(): decrypting files with size: {}", data.length);
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.DECRYPT_MODE, generateKey(encryptionKey, ALGO));
        byte[] decryptedData = cipher.doFinal(data);
        log.info("-decrypt(): decryption done. total size: {}", decryptedData.length);
        return decryptedData;
    }

    private static Key generateKey(final String encryptionKey, final String algo) {
        return new SecretKeySpec(encryptionKey.getBytes(), algo);
    }
}
