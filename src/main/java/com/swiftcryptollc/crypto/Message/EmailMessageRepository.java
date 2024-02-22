package com.swiftcryptollc.crypto.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface EmailMessageRepository extends JpaRepository<EmailMessage, Long> {

    @Query("SELECT u.keyPairBlob FROM User u WHERE u.email = ?1")
    String findKeypairByEmail(String email);
}
