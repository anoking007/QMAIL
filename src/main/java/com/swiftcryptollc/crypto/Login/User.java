package com.swiftcryptollc.crypto.Login;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Lob
    @Column(name = "public_key_bytes", columnDefinition = "BLOB")
    private byte[] publicKeyBytes;

    @Lob
    @Column(name = "private_key_bytes", columnDefinition = "BLOB")
    private byte[] privateKeyBytes;
}