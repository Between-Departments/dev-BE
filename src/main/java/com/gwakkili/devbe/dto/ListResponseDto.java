package com.gwakkili.devbe.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema
public class ListResponseDto<DTO, EN> {

    @Schema(description = "데이터 리스트")
    private List<DTO> dataList;

    public ListResponseDto(List<EN> result, Function<EN, DTO> fn){
        dataList = result.stream().map(fn).collect(Collectors.toList());
    }

}