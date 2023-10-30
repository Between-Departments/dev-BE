package com.gwakkili.devbe.major.service;

import com.gwakkili.devbe.dto.ListResponseDto;
import com.gwakkili.devbe.major.dto.MajorDto;
import com.gwakkili.devbe.major.entity.Major;

import java.util.List;

public interface MajorService {

    ListResponseDto<MajorDto, Major> getNameList(String keyword);

    boolean exist(String major);

}
