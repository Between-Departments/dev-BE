package com.gwakkili.devbe.school.service;

import com.gwakkili.devbe.dto.ListResponseDto;
import com.gwakkili.devbe.school.dto.SchoolDto;
import com.gwakkili.devbe.validation.annotation.School;

public interface SchoolService {

    ListResponseDto<SchoolDto, School> getSchoolList(String keyword);

    String getSchoolMail(String school);

    boolean checkSupportedSchool(String school);
}
