package com.gwakkili.devbe.mail.service;

import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.exception.customExcption.UnsupportedException;
import com.gwakkili.devbe.mail.dto.MailAuthCodeDto;
import com.gwakkili.devbe.mail.entity.MailAuthCode;
import com.gwakkili.devbe.mail.repository.MailAuthCodeRepository;
import com.gwakkili.devbe.school.repository.SchoolRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class MailServiceImpl implements MailService {

    private final MailAuthCodeRepository mailAuthCodeRepository;

    private final SpringTemplateEngine templateEngine;

    private final SchoolRepository schoolRepository;

    private final JavaMailSender mailSender;

    @Value("${AdminMail.id}")
    private String sender;

    @Override
    public void sendMail(String mail) throws MessagingException {
        if (!schoolRepository.existsByMail(mail.split("@")[1]))
            throw new UnsupportedException(ExceptionCode.UNSUPPORTED_MAIL);

        String code = createCode();

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("HI-D 메일 인증번호");
        message.setText(setContext(code), "utf-8", "html");
        message.setFrom(sender);
        mailSender.send(message);

        // AuthKey 저장
        saveAuthCode(mail, code);
    }

    private void saveAuthCode(String mail, String code) {
        MailAuthCode mailAuthCode = MailAuthCode.builder()
                .mail(mail)
                .authCode(code)
                .expiredTime(60 * 60 * 24).build();
        mailAuthCodeRepository.save(mailAuthCode);
    }

    private String setContext(String code) { // 타임리프 설정하는 코드

        Context context = new Context();
        context.setVariable("code", code); // Template에 전달할 데이터 설정
        return templateEngine.process("mail", context); // mail.html
    }

    private String createCode() {
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return Integer.toString(code);
    }


    @Override
    public boolean checkAuthCode(MailAuthCodeDto mailAuthCodeDto) {

        MailAuthCode mailAuthCode = mailAuthCodeRepository.findById(mailAuthCodeDto.getMail())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.MAIL_AUTH_CODE_EXPIRE));

        if (mailAuthCode.getAuthCode().equals(mailAuthCodeDto.getCode())) {
            mailAuthCode.setAuth(true);
            mailAuthCodeRepository.save(mailAuthCode);
            return true;
        }

        return false;
    }

    /**
     * 회원가입시 해당 메일이 인증이 완료 되었는지 롹인하는 메서드
     * @param mail : 인증 완료를 확인할 메일
     * @return : 안증 완료 여부
     */
    @Override
    public boolean checkAuthComplete(String mail) {
        Optional<MailAuthCode> result = mailAuthCodeRepository.findById(mail);
        if (result.isEmpty()) return false;
        MailAuthCode mailAuthCode = result.get();
        return mailAuthCode.isAuth();
    }
}
