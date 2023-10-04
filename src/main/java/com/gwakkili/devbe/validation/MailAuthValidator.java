package com.gwakkili.devbe.validation;

import com.gwakkili.devbe.mail.service.MailService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MailAuthValidator implements ConstraintValidator<MailAuth, String> {

    private final MailService mailService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return mailService.checkAuthComplete(value);
    }
}
