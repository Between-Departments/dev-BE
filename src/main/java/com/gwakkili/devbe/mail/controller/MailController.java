package com.gwakkili.devbe.mail.controller;

import com.gwakkili.devbe.mail.dto.MailAuthCodeDto;
import com.gwakkili.devbe.mail.dto.MailSendDto;
import com.gwakkili.devbe.mail.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/mail")
@Tag(name = "mail", description = "메일 API")
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    @Operation(summary = "메일 전송")
    public void sendMail(@RequestBody @Validated MailSendDto mailSendDto) throws MessagingException, UnsupportedEncodingException {
        mailService.sendMail(mailSendDto.getMail());
    }

    @PostMapping("/confirm")
    @Operation(summary = "메일 인증")
    public boolean authMail(@RequestBody MailAuthCodeDto mailAuthCodeDto) {
        return mailService.checkAuthCode(mailAuthCodeDto);
    }
}
