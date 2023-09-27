package com.gwakkili.devbe.repository;

import com.gwakkili.devbe.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Long> {
    Optional<Major> findByName(String name);
    boolean existsByName(String name);
}
