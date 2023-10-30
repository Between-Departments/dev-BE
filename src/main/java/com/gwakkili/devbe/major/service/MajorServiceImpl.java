package com.gwakkili.devbe.major.service;

import com.gwakkili.devbe.dto.ListResponseDto;
import com.gwakkili.devbe.major.dto.MajorDto;
import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.major.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional
public class MajorServiceImpl implements MajorService {

    private final MajorRepository majorRepository;

    @Override
    public ListResponseDto<MajorDto, Major> getNameList(String keyword) {
        List<Major> majorList = majorRepository.findAllByNameContaining(keyword, PageRequest.of(0, 100));
        Function<Major, MajorDto> fn = (MajorDto::of);
        return new ListResponseDto<>(majorList, fn);
    }

    @Override
    public boolean exist(String major) {
        return majorRepository.existsByName(major);
    }
}
