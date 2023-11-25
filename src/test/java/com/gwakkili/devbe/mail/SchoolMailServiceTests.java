package com.gwakkili.devbe.mail;

import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.exception.customExcption.UnsupportedException;
import com.gwakkili.devbe.mail.dto.MailAuthCodeDto;
import com.gwakkili.devbe.mail.entity.MailAuthCode;
import com.gwakkili.devbe.mail.repository.MailAuthCodeRepository;
import com.gwakkili.devbe.mail.service.MailServiceImpl;
import com.gwakkili.devbe.school.repository.SchoolRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("메일 서비스 테스트")
public class SchoolMailServiceTests {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MailAuthCodeRepository mailAuthCodeRepository;

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private MailServiceImpl mailService;

    @DisplayName("메일 전송 테스트")
    @Nested
    class sendTest {
        @Test
        @DisplayName("메일 전송 성공")
        public void sendSuccessTest() throws MessagingException {
            //given
            String mail = "test@test.com";
            MimeMessage mimeMessage = new MimeMessage((Session) null);
            given(javaMailSender.createMimeMessage()).willReturn(mimeMessage);
            given(schoolRepository.existsByMail(anyString())).willReturn(true);
            given(templateEngine.process(anyString(), any(Context.class))).willReturn(anyString());
            //when
            mailService.sendMail(mail);
            //then
            verify(javaMailSender, times(1)).send(mimeMessage);
        }

        @Test
        @DisplayName("메일 전송 실패 : 지원하지 않는 메일")
        public void FailByUnsupportMailTest() {
            //given
            String mail = "test@test.com";
            given(schoolRepository.existsByMail(anyString())).willReturn(false);

            //when, then
            Assertions.assertThatThrownBy(() -> mailService.sendMail(mail)).isInstanceOf(UnsupportedException.class);
        }
    }

    @DisplayName("인증키 확인 테스트")
    @Nested
    class AuthKeyConfirmTest{
        @Test
        @DisplayName("인증키 확인 성공")
        public void SuccessAuthKeyConfirm() {
            //given
            String mail = "test@test.com";
            String code = "123456";
            MailAuthCodeDto mailAuthCodeDto = new MailAuthCodeDto(mail, code);
            given(mailAuthCodeRepository.findById(mail)).willReturn(Optional.of(MailAuthCode.builder().authCode(code).build()));
            //when, then
            Assertions.assertThat(mailService.checkAuthCode(mailAuthCodeDto)).isTrue();
        }

        @Test
        @DisplayName("인증키 확인 실패 : 인증키 불일치")
        public void failAuthKeyConfirmByDiffAuthKey() {
            //given
            String mail = "test@test.com";
            String code = "123456";
            MailAuthCodeDto mailAuthCodeDto = new MailAuthCodeDto(mail, code);
            String diffAuthKey = "567891";
            given(mailAuthCodeRepository.findById(mail)).willReturn(Optional.of(MailAuthCode.builder().authCode(diffAuthKey).build()));
            //when then
            Assertions.assertThat(mailService.checkAuthCode(mailAuthCodeDto)).isFalse();
        }

        @Test
        @DisplayName("인증키 확인 실패 : 인증키 만료")
        public void failAuthKeyConfirmByAuthKeyExpire() {
            //given
            String mail = "test@test.com";
            String code = "123456";
            MailAuthCodeDto mailAuthCodeDto = new MailAuthCodeDto(mail, code);
            given(mailAuthCodeRepository.findById(mail)).willReturn(Optional.empty());
            //when then
            Assertions.assertThatThrownBy(() -> mailService.checkAuthCode(mailAuthCodeDto)).isInstanceOf(NotFoundException.class);
        }
    }
}
