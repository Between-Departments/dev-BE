package com.gwakkili.devbe.mail.controller;

import com.gwakkili.devbe.mail.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/members/mail")
@Validated
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public void send(@Email @RequestParam(value = "mail") String mail) throws MessagingException, UnsupportedEncodingException {
        mailService.send(mail);
    }

    @GetMapping("/confirm")
    public boolean confirmAuthKey(@RequestParam @Email String mail, String authKey) {
        return mailService.checkAuthKey(mail, authKey);
    }
}
