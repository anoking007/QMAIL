package com.swiftcryptollc.crypto.Message;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "email_message")
public class EmailMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(name = "sender_mail")
    private String senderMail;

    @Column(name = "recipient_mail")
    private String recipientMail;

    @Column(name = "message")
    private String message;

    @Column(name = "time_stamp")
    private Timestamp timestamp;
}
