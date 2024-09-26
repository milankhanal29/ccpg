package com.milan.codechangepresentationgenerator.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY = "milankhanal12345";
    private static final byte[] secretKeyBytes = SECRET_KEY.getBytes();
    private static final byte[] iv = new byte[16]; // 16 bytes for AES

    public static String encrypt(String data) throws Exception {
        // Generate a random IV for each encryption
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] encryptedBytes = cipher.doFinal(data.getBytes("UTF-8"));
        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

        // URL-safe Base64 encoding
        return Base64.getUrlEncoder().withoutPadding().encodeToString(combined);
    }

    public static String decrypt(String encryptedData) throws Exception {
        // URL-safe Base64 decoding
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encryptedData);

        byte[] iv = new byte[16];
        byte[] encryptedBytes = new byte[decodedBytes.length - iv.length];
        System.arraycopy(decodedBytes, 0, iv, 0, iv.length);
        System.arraycopy(decodedBytes, iv.length, encryptedBytes, 0, encryptedBytes.length);

        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, "UTF-8");
    }
}
