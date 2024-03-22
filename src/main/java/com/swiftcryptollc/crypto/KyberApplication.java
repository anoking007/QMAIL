package com.swiftcryptollc.crypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@SpringBootApplication
public class KyberApplication {
    public static void main(String[] args) {
        SpringApplication.run(KyberApplication.class, args);
    }
}
