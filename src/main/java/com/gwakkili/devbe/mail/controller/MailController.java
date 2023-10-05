package com.gwakkili.devbe.mail.controller;

import com.gwakkili.devbe.exception.dto.ExceptionDto;
import com.gwakkili.devbe.mail.dto.MailAuthCodeDto;
import com.gwakkili.devbe.mail.dto.MailSendDto;
import com.gwakkili.devbe.mail.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메일 전송 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "메일 전송 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionDto.class)))
    })
    public void send(@RequestBody @Validated MailSendDto mailSendDto) throws MessagingException, UnsupportedEncodingException {
        log.info("이메일 전송 요청");
        mailService.send(mailSendDto.getMail());
    }

    @PostMapping("/confirm")
    @Operation(summary = "메일 인증")
    public boolean confirmAuthCode(@RequestBody MailAuthCodeDto mailAuthCodeDto) {
        log.info("이메일 인증 요청");
        return mailService.checkAuthCode(mailAuthCodeDto);
    }
}
