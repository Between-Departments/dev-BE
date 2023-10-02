package com.gwakkili.devbe.shcool.repository;

import com.gwakkili.devbe.shcool.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Long> {

    boolean existsByMail(String mail);

    boolean existsByName(String name);

    Optional<School> findByName(String name);

}
