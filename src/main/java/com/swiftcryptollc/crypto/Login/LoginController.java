package com.swiftcryptollc.crypto.Login;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoginController {

    private final UserService userService;

    @PostMapping("/register")
    public String signUp(@RequestBody SignUpRequest signUpRequest) throws IOException {
        return userService.signUp(signUpRequest.getEmail(), signUpRequest.getPassword());
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }
}
