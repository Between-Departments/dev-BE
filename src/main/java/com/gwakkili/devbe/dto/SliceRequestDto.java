package com.gwakkili.devbe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
@AllArgsConstructor
@Builder
public class SliceRequestDto {

    private int page;
    private int size;
    private String keyword;

    public SliceRequestDto(){
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable() {
        return PageRequest.of(page-1, size);
    }
}
