package com.gwakkili.devbe.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@Builder
@Schema
public class SliceRequestDto {

    @Schema(description = "페이지 번호", defaultValue = "1")
    private int page;

    @Schema(description = "페이지 크기", defaultValue = "10")
    private int size;

    @Schema(description = "검색 키워드", example = "안녕")
    private String keyword;

    public SliceRequestDto() {
        this.page = 1;
        this.size = 10;
    }

    @JsonIgnore
    public Pageable getPageable() {
        return PageRequest.of(page - 1, size);
    }

    @JsonIgnore
    public Pageable getPageableDefaultSorting() {
        return PageRequest.of(page -1, size, Sort.Direction.DESC, "createAt");
    }
}
