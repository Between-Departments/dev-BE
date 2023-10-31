package com.gwakkili.devbe.validation.validator;

import com.gwakkili.devbe.member.service.MemberService;
import com.gwakkili.devbe.validation.annotation.NicknameDuplicate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NicknameDuplicateValidator implements ConstraintValidator<NicknameDuplicate, String> {

    private final MemberService memberService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !memberService.nicknameDuplicateCheck(value);
    }
}
