package com.gwakkili.devbe.service;

import com.gwakkili.devbe.entity.MailAuthKey;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.AuthKeyExpireException;
import com.gwakkili.devbe.exception.customExcption.UnupportedMailException;
import com.gwakkili.devbe.repository.MailAuthKeyRepository;
import com.gwakkili.devbe.repository.SchoolRepository;
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
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class MailServiceImpl implements MailService{

    private final MailAuthKeyRepository mailAuthKeyRepository;

    private final SchoolRepository schoolRepository;

    private final JavaMailSender mailSender;

    @Value("${AdminMail.id}")
    private String sender;

    /**
     * 인증 링크 메일을 보내는 메서드
     * @param mail: 인증 링크를 보낼 메일
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @Override
    public void send(String mail) throws MessagingException, UnsupportedEncodingException {
        if(!schoolRepository.existsByMail(mail)) throw new UnupportedMailException(ExceptionCode.UNSUPPORTED_MAIL);

        String authKey = UUID.randomUUID().toString();

        // 메일 전송(html로 변경해야함)
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("과끼리 인증 링크");
        message.setText("http://localhost:8080/members/mail/confirm?mail="+mail+"&authKey=" + authKey, "utf-8", "plain");
        message.setFrom(sender);
        mailSender.send(message);

        // AuthKey 저장
        MailAuthKey mailAuthKey  = MailAuthKey.builder()
                .mail(mail)
                .authKey(authKey)
                .expiredTime(60 * 60 * 24).build();
        mailAuthKeyRepository.save(mailAuthKey);
    }

    /**
     * 메일과 인증키를 이용해 인증을 확인하는 메서드
     * @param mail : 인증 확인을 할 메일
     * @param authKey : 인증 키
     * @return : 인증키 동일 여부
     */
    @Override
    public boolean checkAuthKey(String mail, String authKey) {
        Optional<MailAuthKey> result = mailAuthKeyRepository.findById(mail);
        MailAuthKey mailAuthKey = result.orElseThrow(() -> new AuthKeyExpireException(ExceptionCode.AUTHENTICATION_FAILURE));
        if(mailAuthKey.getAuthKey().equals(authKey)){
            mailAuthKey.setAuth(true);
            mailAuthKeyRepository.save(mailAuthKey);
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
        Optional<MailAuthKey> result = mailAuthKeyRepository.findById(mail);
        MailAuthKey mailAuthKey = result.orElseThrow(() -> new AuthKeyExpireException(ExceptionCode.AUTHENTICATION_FAILURE));
        return mailAuthKey.isAuth();
    }
}
