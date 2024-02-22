package com.swiftcryptollc.crypto.Login;

import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class User {

    @Id
    private String email;

    @Column(name = "password", length = 255)
    private String password;


    @Column(name = "key_pair_blob")
    private String keyPairBlob;

    public String getKeyPairBlob() {
        return keyPairBlob;
    }

    public void setKeyPairBlob(String keyPairBlob) {
        this.keyPairBlob = keyPairBlob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}