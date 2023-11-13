package com.gwakkili.devbe.exception;

import com.gwakkili.devbe.exception.customExcption.CustomException;
import com.gwakkili.devbe.exception.customExcption.IllegalJwtException;
import com.gwakkili.devbe.exception.customExcption.NotFoundJwtException;
import com.gwakkili.devbe.exception.dto.ExceptionDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    //커스텀 에러시 발생
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> customExceptionHandler(CustomException exception) {
        ExceptionDto exceptionDto = new ExceptionDto(exception.getExceptionCode());
        return ResponseEntity.status(exception.getExceptionCode().getHttpStatus()).body(exceptionDto);
    }

    //바인딩 에러시 발생
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> bindExceptionHandler(BindException exception) {
        ExceptionCode code = ExceptionCode.INVALID_INPUT_VALUE;

        Map<String, String> fieldErrors = exception.getBindingResult().getFieldErrors()
                .stream().collect(Collectors.toMap(
                        FieldError::getField,
                        this::getErrorMessage,
                        (f1, f2) -> f1
                ));

        ExceptionDto exceptionDto = new ExceptionDto(code, fieldErrors);
        return ResponseEntity.status(code.getHttpStatus()).body(exceptionDto);
    }

    @MessageExceptionHandler
    @SendToUser("/sub/errors")
    public ExceptionDto CustomMessageExceptionHandler(CustomException exception) {
        log.error(exception.toString());
        return new ExceptionDto(exception.getExceptionCode());
    }

    @MessageExceptionHandler
    @SendToUser("/sub/errors")
    public ExceptionDto AccessDeniedMessageExceptionHandler(AccessDeniedException exception) {
        log.error(exception.toString());
        return new ExceptionDto(ExceptionCode.ACCESS_DENIED);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> AuthenticationExceptionHandler(AuthenticationException exception) {
        log.error(exception.toString());

        ExceptionCode code;
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            code = ExceptionCode.BAD_CREDENTIAL;
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            code = ExceptionCode.AUTHENTICATION_DENIED;
        } else if (exception instanceof LockedException) {
            code = ExceptionCode.AUTHENTICATION_LOCKED;
        } else {
            code = ExceptionCode.AUTHENTICATION_FAILURE;
        }
        ExceptionDto exceptionDto = new ExceptionDto(code);
        return ResponseEntity.status(code.getHttpStatus()).body(exceptionDto);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> JwtExceptionHandler(JwtException exception) {

        ExceptionCode code;
        if (exception instanceof SecurityException || exception instanceof MalformedJwtException) {
            code = ExceptionCode.INVALID_TOKEN;
        } else if (exception instanceof ExpiredJwtException) {
            code = ExceptionCode.EXPIRED_TOKEN;
        } else if (exception instanceof UnsupportedJwtException) {
            code = ExceptionCode.UNSUPPORTED_TOKEN;
        } else if (exception instanceof IllegalJwtException) {
            code = ExceptionCode.ILLEGAL_TOKEN;
        } else if (exception instanceof NotFoundJwtException) {
            code = ExceptionCode.NOT_FOUND_TOKEN;
        } else {
            code = ExceptionCode.AUTHENTICATION_FAILURE;
        }
        ExceptionDto exceptionDto = new ExceptionDto(code);
        return ResponseEntity.status(code.getHttpStatus()).body(exceptionDto);
    }

    //지원하지 않는 HTTP 메소드 호출시 발생
    @ExceptionHandler
    protected ResponseEntity<ExceptionDto> methodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException exception) {
        ExceptionCode code = ExceptionCode.METHOD_NOT_ALLOWED;
        ExceptionDto exceptionDto = new ExceptionDto(code);
        return ResponseEntity.status(code.getHttpStatus()).body(exceptionDto);
    }

    // 타입 불일치로 바인딩 에러시에 발생 -> @RequestParam, @PathVariable, @ModelAttribute
    @ExceptionHandler
    protected ResponseEntity<ExceptionDto> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException exception) {
        ExceptionCode code = ExceptionCode.INVALID_INPUT_VALUE;
        ExceptionDto exceptionDTO = new ExceptionDto(code);
        return ResponseEntity.status(code.getHttpStatus()).body(exceptionDTO);
    }

    @ExceptionHandler
    protected ResponseEntity<ExceptionDto> missingServletRequestParameterException(MissingServletRequestParameterException exception) {
        ExceptionCode code = ExceptionCode.ILLEGAL_FORMAT;
        ExceptionDto exceptionDTO = new ExceptionDto(code);
        exceptionDTO.setMessage(exception.getParameterName() + " 은(는) 필수 입력값입니다.");
        return ResponseEntity.status(code.getHttpStatus()).body(exceptionDTO);
    }

    //입력이 잘못된 형식일떄 발생 -> @RequestBody
    @ExceptionHandler
    protected ResponseEntity<ExceptionDto> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException exception) {
        ExceptionCode code = ExceptionCode.ILLEGAL_FORMAT;
        ExceptionDto exceptionDto = new ExceptionDto(code);
        return ResponseEntity.status(code.getHttpStatus()).body(exceptionDto);
    }

    //메일 전송 실패
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> mailExceptionHandler(MailException exception) {
        ExceptionCode code = ExceptionCode.FAIL_SEND_MAIL;
        ExceptionDto exceptionDto = new ExceptionDto(ExceptionCode.FAIL_SEND_MAIL);
        return ResponseEntity.status(code.getHttpStatus()).body(exceptionDto);
    }

    // DB단에서 발생하는 예외
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> dataAccessExceptionHandler(DataAccessException exception) {
        ExceptionCode code = ExceptionCode.SERVER_ERROR;
        ExceptionDto exceptionDto = new ExceptionDto(ExceptionCode.SERVER_ERROR);
        return ResponseEntity.status(code.getHttpStatus()).body(exceptionDto);
    }


    //errors.properties 에서 값을 읽어오는 함수
    private String getErrorMessage(FieldError error) {
        String[] codes = error.getCodes();
        for (String code : codes) {
            try {
                System.out.println(code);
                return messageSource.getMessage(code, error.getArguments(), Locale.KOREA);
            } catch (NoSuchMessageException ignored) {
            }
        }
        return error.getDefaultMessage();
    }

}
