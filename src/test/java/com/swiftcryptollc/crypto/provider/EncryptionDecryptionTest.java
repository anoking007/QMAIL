package com.swiftcryptollc.crypto.provider;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.security.*;
import javax.crypto.Cipher;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.security.*;

public class EncryptionDecryptionTest {

    @Test
    public void testEncryptionDecryption() {
        try {
            // Generate a key pair
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024); // Adjust key size as needed
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            System.out.println("Public Key: " + Arrays.toString(publicKey.getEncoded()));
            PrivateKey privateKey = keyPair.getPrivate();
            System.out.println("Private Key: " + Arrays.toString(privateKey.getEncoded()));

            // Original data
            String originalData = "Hello, world!";

            // Encrypt data using public key
            byte[] encryptedBytes = encryptData(originalData.getBytes(), publicKey);
            String encryptedData = Base64.getEncoder().encodeToString(encryptedBytes);
            System.out.println("Encrypted Data: " + encryptedData);

            // Decrypt data using private key
            byte[] decryptedBytes = decryptData(Base64.getDecoder().decode(encryptedData), privateKey);
            String decryptedData = new String(decryptedBytes);
            System.out.println("Decrypted Data: " + decryptedData);

            // Verify correctness
            assertEquals(originalData, decryptedData, "Decrypted data doesn't match original data");
        } catch (Exception ex) {
            fail("Exception occurred during testing! [" + ex.getMessage() + "]");
        }
    }

    // Method to encrypt data using a public key
    private byte[] encryptData(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    // Method to decrypt data using a private key
    private byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedData);
    }
}


