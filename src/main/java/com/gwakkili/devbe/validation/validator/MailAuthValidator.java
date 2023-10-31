package com.gwakkili.devbe.validation.validator;

import com.gwakkili.devbe.mail.service.MailService;
import com.gwakkili.devbe.validation.annotation.MailNotAuth;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailAuthValidator implements ConstraintValidator<MailNotAuth, String> {

    private final MailService mailService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return mailService.checkAuthComplete(value);
    }
}
