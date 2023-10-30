package com.gwakkili.devbe.school.repository;

import com.gwakkili.devbe.school.entity.School;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Long> {

    boolean existsByMail(String mail);

    boolean existsByName(String name);

    Optional<School> findByName(String name);

    List<School> findAllByNameContaining(String name, Pageable pageable);

}
