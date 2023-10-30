package com.gwakkili.devbe.major.service;

import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.major.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MajorServiceImpl implements MajorService {

    private final MajorRepository majorRepository;

    @Override
    public List<String> getNameList(String keyword) {
        return majorRepository.findAllByNameContaining(keyword).stream()
                .map(Major::getName).collect(Collectors.toList());
    }

    @Override
    public boolean exist(String major) {
        return majorRepository.existsByName(major);
    }
}
