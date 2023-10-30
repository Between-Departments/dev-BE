package com.gwakkili.devbe.school.service;

import com.gwakkili.devbe.school.entity.School;
import com.gwakkili.devbe.school.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    @Override
    public List<String> getNameList(String keyword) {

        List<School> schools = schoolRepository.findAllByNameContaining(keyword);
        return schools.stream().map(School::getName).collect(Collectors.toList());
    }


    @Override
    public String getSchoolMail(String school) {
        return schoolRepository.findByName(school).map(School::getMail).orElse(null);
    }
}
