package com.gwakkili.devbe.security.repository;

import com.gwakkili.devbe.security.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
