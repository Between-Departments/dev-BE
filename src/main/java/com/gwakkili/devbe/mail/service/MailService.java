package com.gwakkili.devbe.mail.service;

import com.gwakkili.devbe.mail.dto.MailAuthCodeDto;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface MailService {
    void sendMail(String mail) throws MessagingException, UnsupportedEncodingException;

    boolean checkAuthCode(MailAuthCodeDto mailAuthCodeDto);

    boolean checkAuthComplete(String mail);
}
