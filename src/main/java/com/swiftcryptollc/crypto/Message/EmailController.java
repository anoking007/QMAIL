package com.swiftcryptollc.crypto.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.util.Base64;

@RestController
@RequestMapping("/send")
public class EmailController {
    @RequestMapping("/mock")
    public String hello() {
        return "Hello, Mock!";
    }


    private final EmailMessageRepository emailMessageRepository;
    private final EmailService emailService;

    @Autowired
    public EmailController(EmailMessageRepository emailMessageRepository, EmailService emailService) {
        this.emailMessageRepository = emailMessageRepository;
        this.emailService = emailService;
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestBody EmailRequest request) {
        try {
            byte[] publicKeyBytes = emailService.getPublicKeyByEmail(request.getSenderMail());
            byte[] privateKeyBytes = emailService.getPrivateKeyByEmail(request.getSenderMail());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            // Send email and Encrypt
            String secretMessage = request.getMessage();
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] secretMessageBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
            String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);

            // Decryption
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedMessageBytes = decryptCipher.doFinal(Base64.getDecoder().decode(encodedMessage));
            String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
            EmailMessage emailMessage = new EmailMessage();
            emailMessage.setSenderMail(request.getSenderMail());
            emailMessage.setRecipientMail(request.getRecipientMail());
            emailMessage.setMessage(encodedMessage);
            emailMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
            emailMessageRepository.save(emailMessage);
            return "Email sent successfully";
        } catch (Exception ex) {
            return "Failed to send email: " + ex.getMessage();
        }
    }


}
