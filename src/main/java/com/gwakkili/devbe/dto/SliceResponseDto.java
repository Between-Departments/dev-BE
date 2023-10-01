package com.gwakkili.devbe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class SliceResponseDto<DTO, EN> {

    private List<DTO> dataList;

    private int size;

    private boolean hasNext;

    public SliceResponseDto(Slice<EN> result, Function<EN, DTO> fn){
        dataList = result.stream().map(fn).collect(Collectors.toList());
        size = result.getNumberOfElements();
        hasNext = result.hasNext();
    }
}
