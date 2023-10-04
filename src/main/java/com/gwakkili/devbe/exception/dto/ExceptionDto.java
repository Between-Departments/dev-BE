package com.gwakkili.devbe.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gwakkili.devbe.exception.ExceptionCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Map;

/**
 * Exception response 형식
 * 일관된 Exception Response를 응답하려고 사용
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema
public class ExceptionDto {

    @Schema(description = "예외 코드", example = "COMMON-003")
    private String code;

    @Schema(description = "예외 메세지", example = "입력값이 유효하지 않습니다.")
    private String message;

    @Schema(description = "필드 유효성 검사 예외")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> fieldErrors;

    public ExceptionDto(ExceptionCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    public ExceptionDto(ExceptionCode code, Map<String, String> fieldErrors) {
        this.code = code.getCode();
        this.message = code.getMessage();
        this.fieldErrors = fieldErrors;
    }
}
