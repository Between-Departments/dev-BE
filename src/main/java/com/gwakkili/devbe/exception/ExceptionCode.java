package com.gwakkili.devbe.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * http 상태코드로는 모든 exception을 나타낼 수 없기 떄문에 사용
 * http상태코드, 서버정의 코드, 메세지로 구성
 */
@RequiredArgsConstructor
@Getter
public enum ExceptionCode {
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-001", "서버 에러입니다."),
    NOT_SUPPORT_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "COMMON-002", "지원하지 않는 http method 입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON-003", "입력값이 유효하지 않습니다"),
    //인증 예외 코드
    AUTHENTICATION_FAILURE(HttpStatus.UNAUTHORIZED, "AUTH-001", "로그인에 실패하였습니다. 관리자에게 문의하세요."),
    BAD_CREDENTIAL(HttpStatus.UNAUTHORIZED, "AUTH-002", "메일 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요."),
    AUTHENTICATION_LOCKED(HttpStatus.UNAUTHORIZED, "AUTH-003", "정지된 계정입니다. 관리자에게 문의하세요."),
    AUTHENTICATION_DENIED(HttpStatus.UNAUTHORIZED, " AUTH-004", "로그인 요청이 거부되었습니다. 관리자에게 문의하세요."),
    ILLEGAL_AUTHENTICATION_FORMAT(HttpStatus.UNAUTHORIZED, "AUTH-005", "잘못된 로그인 요청 형식입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-006", "만료된 JWT 토큰 입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-007", "유효하지 않은 JWT 토큰 입니다."),
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-008", "JWT 토큰을 찾을 수 없습니다."),
    //이미지 예외 코드
    NOT_IMAGE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "IMAGE_001", "이미지 파일이 아닙니다."),
    FAIL_UPLOAD(HttpStatus.BAD_REQUEST, "IMAGE_002", "이미지 업로드에 실패하였습니다."),
    // 메일 예외 코드
    UNSUPPORTED_MAIL(HttpStatus.NOT_FOUND, "MAIL_001", "지원하지 않는 학교 메일입니다."),
    MAIL_AUTH_LINK_EXPIRE(HttpStatus.NOT_FOUND, "MAIL_002", "메일 인증 링크가 만료되었습니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "USER-001", "인증되지 않은 사용자입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "USER-003", "접근이 거부되었습니다.");



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
