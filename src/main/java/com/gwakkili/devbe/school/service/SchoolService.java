package com.gwakkili.devbe.school.service;

import com.gwakkili.devbe.dto.ListResponseDto;
import com.gwakkili.devbe.school.dto.SchoolDto;
import com.gwakkili.devbe.validation.School;

import java.util.List;

public interface SchoolService {

    ListResponseDto<SchoolDto, School> getNameList(String keyword);

    String getSchoolMail(String school);
}
