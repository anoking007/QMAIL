package com.swiftcryptollc.crypto.Login;

import com.swiftcryptollc.crypto.provider.Kyber1024KeyPairGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Base64;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String signUp(String email, String password) throws IOException {
        if (userRepository.existsByEmail(email)) {
            return "Email already exists";
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        Kyber1024KeyPairGenerator keyGen1024 = new Kyber1024KeyPairGenerator();
        KeyPair keyPair = keyGen1024.generateKeyPair();
        byte[] keyPairBytes = serializeKeyPair(keyPair);
        String keyPairBase64String = Base64.getEncoder().encodeToString(keyPairBytes);
        user.setKeyPairBlob(keyPairBase64String);
        userRepository.save(user);
        return "User registered successfully";
    }

    public String login(String email, String password) {
        // Find user by email
        User user = userRepository.findByEmail(email);

        // Check if user exists and password matches
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return "Login successful";
        } else {
            return "Invalid email or password";
        }
    }

    public byte[] serializeKeyPair(KeyPair keyPair) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(keyPair);
        oos.flush();
        return bos.toByteArray();
    }
}
