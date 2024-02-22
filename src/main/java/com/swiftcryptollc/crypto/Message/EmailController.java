package com.swiftcryptollc.crypto.Message;

import com.swiftcryptollc.crypto.provider.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.KeyPair;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/send")
public class EmailController {
    @RequestMapping("/mock")
    public String hello() {
        return "Hello, Mock!";
    }

    private final EmailMessageRepository emailMessageRepository;

    @Autowired
    public EmailController(EmailMessageRepository emailMessageRepository) {
        this.emailMessageRepository = emailMessageRepository;
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestBody EmailRequest request) {
        try {
            EmailMessage emailMessage = new EmailMessage();
            emailMessage.setSenderMail(request.getSenderMail());
            emailMessage.setRecipientMail(request.getRecipientMail());

            Kyber1024KeyPairGenerator bobKeyGen1024 = new Kyber1024KeyPairGenerator();
            KeyPair bobKeyPair = bobKeyGen1024.generateKeyPair();
            KyberPublicKey bobPublicKey = (KyberPublicKey) bobKeyPair.getPublic();
            KyberPrivateKey bobPrivateKey = (KyberPrivateKey) bobKeyPair.getPrivate();


            Kyber1024KeyPairGenerator aliceKeyGen1024 = new Kyber1024KeyPairGenerator();
            KeyPair aliceKeyPair = aliceKeyGen1024.generateKeyPair();
            KyberPrivateKey alicePrivateKey = (KyberPrivateKey) aliceKeyPair.getPrivate();

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


            emailMessage.setMessage(Arrays.toString(aliceEncodedCipherText));
            emailMessage.setTimestamp(Timestamp.from(Instant.now()));
            emailMessageRepository.save(emailMessage);
            return "Email sent successfully";
        } catch (Exception ex) {
            return "Failed to send email: " + ex.getMessage();
        }
    }

    public KeyPair deserializeKeyPair(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (KeyPair) ois.readObject();
    }
}
