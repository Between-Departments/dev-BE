package com.gwakkili.devbe.mail;

import com.gwakkili.devbe.mail.entity.MailAuthCode;
import com.gwakkili.devbe.mail.repository.MailAuthCodeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.util.Optional;

@DataRedisTest
@DisplayName("인증키 저장소 테스트")
@MockBean(JpaMetamodelMappingContext.class)
public class SchoolMailAuthCodeRepositoryTests {

    @Autowired
    private MailAuthCodeRepository mailAuthCodeRepository;


    @DisplayName("인증키 저장")
    @Test
    public void save() {
        //given
        MailAuthCode mailAuthCode = MailAuthCode.builder()
                .mail("test@test.com")
                .authCode("111111")
                .expiredTime(60)
                .build();
        //when
        MailAuthCode save = mailAuthCodeRepository.save(mailAuthCode);
        //then
        Assertions.assertThat(save).isSameAs(mailAuthCode);
    }

    @DisplayName("인증여부 변경")
    @Test
    public void update(){
        //given
        MailAuthCode mailAuthCode = MailAuthCode.builder()
                .mail("test@test.com")
                .authCode("111111")
                .expiredTime(60)
                .build();
        MailAuthCode save = mailAuthCodeRepository.save(mailAuthCode);
        //when
        save.setAuth(true);
        mailAuthCodeRepository.save(save);
        //then
        Optional<MailAuthCode> find = mailAuthCodeRepository.findById(save.getMail());
        Assertions.assertThat(find.get().isAuth()).isTrue();
    }

    @DisplayName("인증키 만료 테스트")
    @Test
    public void expire() throws InterruptedException {
        //given
        MailAuthCode mailAuthCode = MailAuthCode.builder()
                .mail("test@test.com")
                .authCode("111111")
                .expiredTime(1)
                .build();
        MailAuthCode save = mailAuthCodeRepository.save(mailAuthCode);
        //when
        Thread.sleep(1000);
        boolean exist = mailAuthCodeRepository.existsById(save.getMail());
        //then
        Assertions.assertThat(exist).isFalse();
    }
}
