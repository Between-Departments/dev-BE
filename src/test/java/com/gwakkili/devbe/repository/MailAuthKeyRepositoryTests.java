package com.gwakkili.devbe.repository;

import com.gwakkili.devbe.entity.MailAuthKey;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.util.Optional;
import java.util.UUID;

@DataRedisTest
@DisplayName("인증키 저장소 테스트")
@MockBean(JpaMetamodelMappingContext.class)
public class MailAuthKeyRepositoryTests {

    @Autowired
    private MailAuthKeyRepository mailAuthKeyRepository;


    @DisplayName("인증키 저장")
    @Test
    public void save(){
        //given
        MailAuthKey mailAuthKey = MailAuthKey.builder()
                .mail("test@test.com")
                .authKey(UUID.randomUUID().toString())
                .expiredTime(60)
                .build();
        //when
        MailAuthKey save = mailAuthKeyRepository.save(mailAuthKey);
        //then
        Assertions.assertThat(save).isSameAs(mailAuthKey);
    }

    @DisplayName("인증여부 변경")
    @Test
    public void update(){
        //given
        MailAuthKey mailAuthKey = MailAuthKey.builder()
                .mail("test@test.com")
                .authKey(UUID.randomUUID().toString())
                .expiredTime(60)
                .build();
        MailAuthKey save = mailAuthKeyRepository.save(mailAuthKey);
        //when
        save.setAuth(true);
        mailAuthKeyRepository.save(save);
        //then
        Optional<MailAuthKey> find = mailAuthKeyRepository.findById(save.getMail());
        Assertions.assertThat(find.get().isAuth()).isTrue();
    }

    @DisplayName("인증키 만료 테스트")
    @Test
    public void expire() throws InterruptedException {
        //given
        MailAuthKey mailAuthKey = MailAuthKey.builder()
                .mail("test@test.com")
                .authKey(UUID.randomUUID().toString())
                .expiredTime(1)
                .build();
        MailAuthKey save = mailAuthKeyRepository.save(mailAuthKey);
        //when
        Thread.sleep(1000);
        boolean exist = mailAuthKeyRepository.existsById(save.getMail());
        //then
        Assertions.assertThat(exist).isFalse();
    }
}
