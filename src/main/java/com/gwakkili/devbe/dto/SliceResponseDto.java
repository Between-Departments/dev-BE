package com.gwakkili.devbe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Schema
public class SliceResponseDto<DTO, EN> {

    @Schema(description = "데이터 리스트")
    private List<DTO> dataList;

    @Schema(description = "데이터 크기")
    private int size;

    @Schema(description = "다음 페이지 존재여부")
    private boolean hasNext;

    public SliceResponseDto(Slice<EN> result, Function<EN, DTO> fn){
        dataList = result.stream().map(fn).collect(Collectors.toList());
        size = result.getNumberOfElements();
        hasNext = result.hasNext();
    }
}
