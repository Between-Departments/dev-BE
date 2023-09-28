package com.gwakkili.devbe.mail.controller;

import com.gwakkili.devbe.mail.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members/mail")
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public void send(String mail) throws MessagingException, UnsupportedEncodingException {
        mailService.send(mail);
    }

    @GetMapping("/confirm")
    public boolean confirmAuthKey(String mail, String authKey){
        return mailService.checkAuthKey(mail, authKey);
    }
}
