package com.gwakkili.devbe.service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface MailService {
    void send(String mail)throws MessagingException, UnsupportedEncodingException;
    boolean checkAuthKey(String mail, String authKey);
    boolean checkAuthComplete(String mail);
}
