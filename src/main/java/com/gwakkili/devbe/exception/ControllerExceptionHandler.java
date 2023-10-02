package com.gwakkili.devbe.exception;

import com.gwakkili.devbe.exception.dto.ExceptionDto;
import com.gwakkili.devbe.exception.customExcption.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Set;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    //커스텀 에러시 발생
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> customExceptionHandler(CustomException exception){
        log.error(exception.toString());
        ExceptionDto exceptionDto = new ExceptionDto(exception.getExceptionCode());
        return ResponseEntity.status(exception.getExceptionCode().getHttpStatus()).body(exceptionDto);
    }

    //바인딩 에러시 발생
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> bindExceptionHandler(BindException exception) {
        log.error(exception.toString());
        ExceptionCode code = ExceptionCode.INVALID_INPUT_VALUE;
        ExceptionDto exceptionDto = new ExceptionDto(code, exception.getBindingResult());
        return ResponseEntity.status(code.getHttpStatus()).body(exceptionDto);
    }

    //메일 전송 실패
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> mailExceptionHandler(MailException exception) {
        log.error(exception.toString());
        ExceptionCode code = ExceptionCode.FAIL_SEND_MAIL;
        ExceptionDto exceptionDto = new ExceptionDto(ExceptionCode.FAIL_SEND_MAIL);
        return ResponseEntity.status(code.getHttpStatus()).body(exceptionDto);
    }

}
