package com.swiftcryptollc.crypto.Message;

import com.swiftcryptollc.crypto.Login.User;
import com.swiftcryptollc.crypto.Login.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class EmailService {

    private UserRepository emailMessageRepository;

    public byte[] getPublicKeyByEmail(String email) {
        User optionalUser = emailMessageRepository.findByEmail(email);
        return optionalUser.getPublicKeyBytes();
    }

    public byte[] getPrivateKeyByEmail(String email) {
        User optionalUser = emailMessageRepository.findByEmail(email);
        return optionalUser.getPrivateKeyBytes();
    }
}
