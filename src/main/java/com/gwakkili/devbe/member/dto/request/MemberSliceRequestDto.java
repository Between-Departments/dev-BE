package com.gwakkili.devbe.member.dto.request;

import com.gwakkili.devbe.dto.SliceRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Sort;


@Data
@EqualsAndHashCode(callSuper = true)
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
