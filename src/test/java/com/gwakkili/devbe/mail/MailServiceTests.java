package com.gwakkili.devbe.mail;

import com.gwakkili.devbe.mail.dto.MailAuthCodeDto;
import com.gwakkili.devbe.mail.entity.MailAuthCode;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.exception.customExcption.UnsupportedException;
import com.gwakkili.devbe.mail.service.MailServiceImpl;
import com.gwakkili.devbe.mail.repository.MailAuthKeyRepository;
import com.gwakkili.devbe.shcool.repository.SchoolRepository;
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

import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("메일 서비스 테스트")
public class MailServiceTests {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MailAuthKeyRepository mailAuthKeyRepository;

    @InjectMocks
    private MailServiceImpl mailService;

    @DisplayName("메일 전송 테스트")
    @Nested
    class sendTest{
        @Test
        @DisplayName("메일 전송 성공")
        public void sendSuccessTest() throws MessagingException {
            //given
            String mail = "test@test.com";
            MimeMessage mimeMessage = new MimeMessage((Session) null);
            given(javaMailSender.createMimeMessage()).willReturn(mimeMessage);
            given(schoolRepository.existsByMail(anyString())).willReturn(true);

            //when, then
            mailService.send(mail);
        }

        @Test
        @DisplayName("메일 전송 실패 : 지원하지 않는 메일")
        public void sendFailUnsupportMailTest() {
            //given
            String mail = "test@test.com";
            given(schoolRepository.existsByMail(anyString())).willReturn(false);

            //when, then
            Assertions.assertThatThrownBy(() -> mailService.send(mail)).isInstanceOf(UnsupportedException.class);
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
            MailAuthCodeDto mailAuthCodeDto = MailAuthCodeDto.builder().mail(mail).code(code).build();
            given(mailAuthKeyRepository.findById(mail)).willReturn(Optional.of(MailAuthCode.builder().authCode(code).build()));
            //when, then
            Assertions.assertThat(mailService.checkAuthCode(mailAuthCodeDto)).isTrue();
        }

        @Test
        @DisplayName("인증키 확인 실패 : 인증키 불일치")
        public void failAuthKeyConfirmByDiffAuthKey() {
            //given
            String mail = "test@test.com";
            String code = "123456";
            MailAuthCodeDto mailAuthCodeDto = MailAuthCodeDto.builder().mail(mail).code(code).build();
            String diffAuthKey = "567891";
            given(mailAuthKeyRepository.findById(mail)).willReturn(Optional.of(MailAuthCode.builder().authCode(diffAuthKey).build()));
            //when then
            Assertions.assertThat(mailService.checkAuthCode(mailAuthCodeDto)).isFalse();
        }

        @Test
        @DisplayName("인증키 확인 실패 : 인증키 만료")
        public void failAuthKeyConfirmByAuthKeyExpire() {
            //given
            String mail = "test@test.com";
            String code = "123456";
            MailAuthCodeDto mailAuthCodeDto = MailAuthCodeDto.builder().mail(mail).code(code).build();
            given(mailAuthKeyRepository.findById(mail)).willReturn(Optional.empty());
            //when then
            Assertions.assertThatThrownBy(() -> mailService.checkAuthCode(mailAuthCodeDto)).isInstanceOf(NotFoundException.class);
        }
    }
}
