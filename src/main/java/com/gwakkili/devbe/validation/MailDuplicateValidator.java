package com.gwakkili.devbe.validation;

import com.gwakkili.devbe.member.service.MemberService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailDuplicateValidator implements ConstraintValidator<MailDuplicate, String> {

    private final MemberService memberService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !memberService.mailDuplicateCheck(value);
    }
}
