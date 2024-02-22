package com.swiftcryptollc.crypto.provider;

import java.security.KeyPair;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the key agreement flow
 *
 * @author Steven K Fisher <swiftcryptollc@gmail.com>
 */
public class KeyAgreementTest {

    /**
     * Bob sends public key to Alice
     * <p>
     * Alice generates KyberEncrypted(secret, cipher) from Bob's public Key and
     * her private Key
     * <p>
     * Alice sends Bob the cipher text
     * <p>
     * Bob decrypts the cipher text using his private key to get the secret key
     */
    @Test
    public void testKeyAgreement() {
        try {
            //alice is a sender and bob is recipient
            Kyber1024KeyPairGenerator bobKeyGen1024 = new Kyber1024KeyPairGenerator();
            KeyPair bobKeyPair = bobKeyGen1024.generateKeyPair();
            KyberPublicKey bobPublicKey = (KyberPublicKey) bobKeyPair.getPublic();
            KyberPrivateKey bobPrivateKey = (KyberPrivateKey) bobKeyPair.getPrivate();



            Kyber1024KeyPairGenerator aliceKeyGen1024 = new Kyber1024KeyPairGenerator();
            KeyPair aliceKeyPair = aliceKeyGen1024.generateKeyPair();
            KyberPrivateKey alicePrivateKey = (KyberPrivateKey) aliceKeyPair.getPrivate();



            // Bob sends Alice his encoded public Key
            byte[] bobEncodedPublicKey = bobPublicKey.getEncoded();


            // Alice initiates a Key Agreement with Bob
            KyberKeyAgreement aliceKeyAgreement = new KyberKeyAgreement();
            aliceKeyAgreement.engineInit(alicePrivateKey);
            // Generated CipherText and SecretKey from Bob's public Key and Alice's Private Key
            KyberEncrypted aliceCipherSecret = (KyberEncrypted) aliceKeyAgreement.engineDoPhase(new KyberPublicKey(bobEncodedPublicKey), true);
            KyberSecretKey aliceGeneratedSecretKey = aliceCipherSecret.getSecretKey();
            KyberCipherText aliceCipherText = aliceCipherSecret.getCipherText();
            System.out.println(aliceCipherText);
            // Send Alice's generated encoded Cipher Text to Bob
            // Bob initializes his own key agreement
            byte[] aliceEncodedCipherText = aliceCipherText.getEncoded();
            System.out.println(Arrays.toString(aliceEncodedCipherText));
            KyberKeyAgreement bobKeyAgreement = new KyberKeyAgreement();
            bobKeyAgreement.engineInit(bobPrivateKey);
            // Decrypt the ciphertext back into the secret key
            KyberDecrypted bobKyberDecrypted = (KyberDecrypted) bobKeyAgreement.engineDoPhase(new KyberCipherText(aliceEncodedCipherText), true);
            System.out.println(bobKyberDecrypted);
            KyberSecretKey bobGeneratedSecretKey = bobKyberDecrypted.getSecretKey();
            System.out.println(bobGeneratedSecretKey);
            assertEquals(0, Utilities.constantTimeCompare(aliceGeneratedSecretKey.getS(), bobGeneratedSecretKey.getS()));
        } catch (Exception ex) {
            fail("Exception occured during the test! [" + ex.getMessage() + "]");
        }
    }
}
