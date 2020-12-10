package be.howest.ti.mars.logic.util;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TokenAES {

    private static final Logger LOGGER = Logger.getLogger(TokenAES.class.getName());
    private static final Cipher cipher;

    private TokenAES() {
        throw new IllegalStateException("Utility class");
    }

    static {
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new MarsException("Couldn't get cipher instance!");
        }
    }

    private static final SecretKey secretKey;

    static {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            secretKey = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new MarsException("Couldn't get key instance!");
        }
    }

    public static String encrypt(String plainText) {
        byte[] plainTextByte = plainText.getBytes();
        byte[] encryptedByte;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedByte = cipher.doFinal(plainTextByte);
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new MarsException("Failed to encrypt!");
        }
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(encryptedByte);
    }

    public static String decrypt(String encryptedText) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        byte[] decryptedByte;
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedByte = cipher.doFinal(encryptedTextByte);
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new MarsException("Failed to decrypt!");
        }
        return new String(decryptedByte);
    }
}
