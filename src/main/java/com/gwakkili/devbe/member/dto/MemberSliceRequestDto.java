package com.gwakkili.devbe.member.dto;

import com.gwakkili.devbe.dto.SliceRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class MemberSliceRequestDto extends SliceRequestDto {

    @Schema(description = "검색 키워드", example = "sds@test.a")
    private String keyword;

    @Builder
    public MemberSliceRequestDto(int page, int size, String sortBy, Sort.Direction direction, String keyword) {
        super(page, size, sortBy, direction);
        this.keyword = keyword;
    }

    public MemberSliceRequestDto() {
        super();
    }

}
