package com.gwakkili.devbe.mail.service;

import com.gwakkili.devbe.mail.dto.MailAuthCodeDto;
import com.gwakkili.devbe.mail.entity.MailAuthCode;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.exception.customExcption.UnsupportedException;
import com.gwakkili.devbe.mail.repository.MailAuthCodeRepository;
import com.gwakkili.devbe.shcool.repository.SchoolRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class MailServiceImpl implements MailService {

    private final MailAuthCodeRepository mailAuthCodeRepository;

    private final SchoolRepository schoolRepository;

    private final JavaMailSender mailSender;

    @Value("${AdminMail.id}")
    private String sender;

    /**
     * 인증 링크 메일을 보내는 메서드
     *
     * @param mail: 인증 링크를 보낼 메일
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @Override
    public void send(String mail) throws MessagingException {
        if (!schoolRepository.existsByMail(mail.split("@")[1]))
            throw new UnsupportedException(ExceptionCode.UNSUPPORTED_MAIL);

        String authCode = createCode();

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("과끼리 인증 번호");
        message.setText(authCode);
        message.setFrom(sender);
        mailSender.send(message);

        // AuthKey 저장
        MailAuthCode mailAuthCode = MailAuthCode.builder()
                .mail(mail)
                .authCode(authCode)
                .expiredTime(60 * 60 * 24).build();
        mailAuthCodeRepository.save(mailAuthCode);
    }

    private String createCode() {
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return Integer.toString(code);
    }


    @Override
    public boolean checkAuthCode(MailAuthCodeDto mailAuthCodeDto) {
        Optional<MailAuthCode> result = mailAuthCodeRepository.findById(mailAuthCodeDto.getMail());
        MailAuthCode mailAuthCode = result.orElseThrow(() -> new NotFoundException(ExceptionCode.MAIL_AUTH_CODE_EXPIRE));
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
