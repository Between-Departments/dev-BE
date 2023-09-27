package com.gwakkili.devbe.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gwakkili.devbe.exception.ExceptionCode;
import lombok.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Exception response 형식
 * 일관된 Exception Response를 응답하려고 사용
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ExceptionDto {
    private String code;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> fieldErrors;

    public ExceptionDto(ExceptionCode code){
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    public ExceptionDto(ExceptionCode code, BindingResult bindingResult){
        this.code = code.getCode();
        this.message = code.getMessage();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        this.fieldErrors  = fieldErrors.stream().distinct().collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getDefaultMessage,
                (f1, f2) -> f1
        ));
    }
}
