package com.gwakkili.devbe.school.repository;

import com.gwakkili.devbe.school.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School, Long> {

    boolean existsByMail(String mail);

    boolean existsByName(String name);

    List<School> findAllByNameContaining(String name);

}
