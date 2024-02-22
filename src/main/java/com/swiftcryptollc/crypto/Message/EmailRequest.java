package com.swiftcryptollc.crypto.Message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {
    private String senderMail;
    private String recipientMail;
    private String message;

}
