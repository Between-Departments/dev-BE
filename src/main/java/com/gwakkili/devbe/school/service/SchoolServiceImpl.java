package com.gwakkili.devbe.school.service;

import com.gwakkili.devbe.dto.ListResponseDto;
import com.gwakkili.devbe.school.dto.SchoolDto;
import com.gwakkili.devbe.school.entity.School;
import com.gwakkili.devbe.school.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    @Override
    public ListResponseDto<SchoolDto, com.gwakkili.devbe.validation.School> getNameList(String keyword) {

        List<School> schoolList = schoolRepository.findAllByNameContaining(keyword, PageRequest.of(0, 100));
        Function<School, SchoolDto> fn = (SchoolDto::of);
        return new ListResponseDto(schoolList, fn);
    }


    @Override
    public String getSchoolMail(String school) {
        return schoolRepository.findByName(school).map(School::getMail).orElse(null);
    }
}
