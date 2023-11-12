package com.gwakkili.devbe.major.repository;

import com.gwakkili.devbe.major.entity.Major;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MajorRepository extends JpaRepository<Major, Long> {

    boolean existsByName(String name);

    List<Major> findAllByNameContaining(String name, Pageable pageable);
}
