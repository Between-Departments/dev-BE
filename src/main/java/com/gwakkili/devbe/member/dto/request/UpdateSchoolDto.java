package com.gwakkili.devbe.member.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.validation.MailAuth;
import com.gwakkili.devbe.validation.MailDuplicate;
import com.gwakkili.devbe.validation.MailMissMatch;
import com.gwakkili.devbe.validation.School;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
@Schema
@MailMissMatch(fieldName1 = "school", fieldName2 = "newMail")
public class UpdateSchoolDto {

    @JsonIgnore
    long memberId;

    @MailDuplicate
    @MailAuth
    @Email
    @Schema(description = "새로운 메일", example = "test1@sun.ac.kr")
    private String newMail;

    @School
    @Schema(description = "학교", example = "연세대학교")
    private String school;
}
