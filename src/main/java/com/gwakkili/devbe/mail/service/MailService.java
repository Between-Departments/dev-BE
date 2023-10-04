package com.gwakkili.devbe.mail.service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface MailService {
    void send(String mail) throws MessagingException, UnsupportedEncodingException;

    boolean checkAuthCode(String mail, String authCode);

    boolean checkAuthComplete(String mail);
}
