package com.swiftcryptollc.crypto.Message;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class EmailRequest {
    private String senderMail;
    private String recipientMail;
    private String message;
}
