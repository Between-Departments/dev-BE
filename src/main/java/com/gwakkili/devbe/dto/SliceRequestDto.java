package com.gwakkili.devbe.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@Data
@Schema
public class SliceRequestDto {

    @Schema(description = "페이지 번호", defaultValue = "1")
    protected int page;

    @Schema(description = "페이지 크기", defaultValue = "10")
    protected int size;

    @Schema(description = "정렬 기준", example = "recommendCount")
    private String sortBy;

    @Schema(description = "정렬 방향", example = "DESC")
    private Sort.Direction direction;

    public SliceRequestDto() {
        this.page = 1;
        this.size = 10;
        this.direction = Sort.Direction.DESC;
    }


    @JsonIgnore
    public Pageable getPageable() {
        return (sortBy == null) ? PageRequest.of(page - 1, size) :
                PageRequest.of(page - 1, size, direction, sortBy);
    }

    @JsonIgnore
    public Pageable getPageableDefaultSorting() {
        return PageRequest.of(page -1, size, Sort.Direction.DESC, "createAt");
    }
}
