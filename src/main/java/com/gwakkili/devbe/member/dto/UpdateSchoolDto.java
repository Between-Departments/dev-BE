package com.gwakkili.devbe.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.validation.MailAuth;
import com.gwakkili.devbe.validation.MailDuplicate;
import com.gwakkili.devbe.validation.School;
import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UpdateSchoolDto {

    @JsonIgnore
    private String mail;

    @MailDuplicate
    @MailAuth
    @Email
    private String newMail;

    @School
    private String school;
}
