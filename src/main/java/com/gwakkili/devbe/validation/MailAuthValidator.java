package com.gwakkili.devbe.validation;

import com.gwakkili.devbe.mail.entity.MailAuthKey;
import com.gwakkili.devbe.mail.repository.MailAuthKeyRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MailAuthValidator implements ConstraintValidator<MailAuth, String> {

    private final MailAuthKeyRepository mailAuthKeyRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<MailAuthKey> optionalMailAuthKey = mailAuthKeyRepository.findById(value);
        return optionalMailAuthKey.isPresent() && optionalMailAuthKey.get().isAuth();
    }
}
