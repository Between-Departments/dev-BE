package com.gwakkili.devbe.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@Schema
@AllArgsConstructor
public class SliceRequestDto {

    @Schema(description = "페이지 번호", defaultValue = "1")
    protected int page;

    @Schema(description = "페이지 크기", defaultValue = "10")
    protected int size;

    @Schema(description = "정렬 기준", defaultValue = "createAt")
    private String sortBy;

    @Schema(description = "정렬 방향", defaultValue = "DESC")
    private Sort.Direction direction;

    public SliceRequestDto() {
        this.page = 1;
        this.size = 10;
        this.direction = Sort.Direction.DESC;
        this.sortBy = "createAt";
    }


    @JsonIgnore
    public Pageable getPageable() {
        return (sortBy == null) ? PageRequest.of(page - 1, size) :
                PageRequest.of(page - 1, size, direction, sortBy);
    }

}
