package com.gwakkili.devbe.major.service;

import com.gwakkili.devbe.dto.ListResponseDto;
import com.gwakkili.devbe.major.dto.MajorDto;
import com.gwakkili.devbe.major.entity.Major;

public interface MajorService {

    ListResponseDto<MajorDto, Major> getMajorList(String keyword);

    boolean checkSupportedMajor(String major);

}
