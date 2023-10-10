package com.gwakkili.devbe.mail.repository;

import com.gwakkili.devbe.mail.entity.MailAuthCode;
import org.springframework.data.repository.CrudRepository;

public interface MailAuthCodeRepository extends CrudRepository<MailAuthCode, String> {
}
