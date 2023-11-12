package com.gwakkili.devbe.member.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.validation.annotation.MailDuplicate;
import com.gwakkili.devbe.validation.annotation.MailMissMatch;
import com.gwakkili.devbe.validation.annotation.MailNotAuth;
import com.gwakkili.devbe.validation.annotation.School;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema
@MailMissMatch(fieldName1 = "school", fieldName2 = "newMail")
public class UpdateSchoolDto {

    @JsonIgnore
    long memberId;

    @Email
    @NotBlank
    @MailNotAuth
    @MailDuplicate
    @Schema(description = "새로운 메일", example = "test1@sun.ac.kr")
    private String newMail;

    @School
    @NotBlank
    @Schema(description = "학교", example = "연세대학교")
    private String school;

    public UpdateSchoolDto(String newMail, String school) {
        this.newMail = newMail;
        this.school = school;
    }
}
