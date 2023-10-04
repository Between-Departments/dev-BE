package com.gwakkili.devbe.mail.repository;

import com.gwakkili.devbe.mail.entity.MailAuthCode;
import org.springframework.data.repository.CrudRepository;

public interface MailAuthKeyRepository extends CrudRepository<MailAuthCode, String> {
}
