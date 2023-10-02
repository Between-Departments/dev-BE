package com.gwakkili.devbe.major.repository;

import com.gwakkili.devbe.major.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Long> {
    Optional<Major> findByName(String name);

    boolean existsByName(String name);

    List<Major> findAllByNameContaining(String name);
}
