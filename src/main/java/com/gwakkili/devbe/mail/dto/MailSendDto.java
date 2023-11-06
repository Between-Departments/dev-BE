package com.gwakkili.devbe.mail.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailSendDto {

    @NotBlank
    @Email
    String mail;
}
