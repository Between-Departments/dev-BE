package com.gwakkili.devbe.repository;

import com.gwakkili.devbe.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {

    boolean existsByMail(String mail);
}
