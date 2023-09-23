package com.gwakkili.devbe.repository;

import com.gwakkili.devbe.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
