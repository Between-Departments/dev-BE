package com.gwakkili.devbe.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * http 상태코드로는 모든 exception을 나타낼 수 없기 떄문에 사용
 * http상태코드, 서버정의 코드, 메세지로 구성
 */
@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    //일반 예외 코드
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-001", "서버 에러입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON-002", "지원하지 않는 http method 입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON-003", "입력값이 유효하지 않습니다"),
    ILLEGAL_FORMAT(HttpStatus.BAD_REQUEST, "COMMON-004", "잘못된 요청 형식 입니다."),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "COMMON-005", "지원하지 않는 미디어 타입입니다."),

    //인증 예외 코드
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH-001", "인증되지 않은 사용자입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH-002", "접근이 거부되었습니다."),
    AUTHENTICATION_FAILURE(HttpStatus.UNAUTHORIZED, "AUTH-003", "로그인에 실패하였습니다. 관리자에게 문의하세요."),
    BAD_CREDENTIAL(HttpStatus.UNAUTHORIZED, "AUTH-004", "메일 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요."),
    AUTHENTICATION_LOCKED(HttpStatus.UNAUTHORIZED, "AUTH-005", "정지된 계정입니다. 관리자에게 문의하세요."),
    AUTHENTICATION_DENIED(HttpStatus.UNAUTHORIZED, " AUTH-006", "로그인 요청이 거부되었습니다. 관리자에게 문의하세요."),
    ILLEGAL_AUTHENTICATION_FORMAT(HttpStatus.UNAUTHORIZED, "AUTH-007", "잘못된 로그인 요청 형식입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-008", "만료된 JWT 토큰 입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-009", "유효하지 않은 JWT 토큰 입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-010", "지원하지 않는 JWT 토큰입니다."),
    ILLEGAL_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-011", "잘못된 JWT 토큰입니다."),
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-012", "JWT 토큰을 찾을 수 없습니다."),
    //이미지 예외 코드
    FAIL_UPLOAD(HttpStatus.BAD_REQUEST, "IMAGE_002", "이미지 업로드에 실패하였습니다."),

    // 메일 예외 코드
    UNSUPPORTED_MAIL(HttpStatus.NOT_FOUND, "MAIL_001", "지원하지 않는 학교 메일입니다."),
    MAIL_AUTH_CODE_EXPIRE(HttpStatus.NOT_FOUND, "MAIL_002", "메일 인증 번호가 만료되었습니다."),
    FAIL_SEND_MAIL(HttpStatus.BAD_REQUEST, "MAIL_003", " 메일 전송에 실패하였습니다."),

    // 회원 예외 코드
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "MEMBER_001", "해당 계정을 찾을 수 없습니다."),
    DUPLICATE_MAIL(HttpStatus.CONFLICT, "MEMBER_002", "이미 가입된 메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "MEMBER_003", "이미 가입된 닉네임입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER_004", "비밀번호가 올바르지 않습니다."),

    // 게시글 예외 코드
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "POST-001", "게시물을 찾을 수 없습니다."),

    //신고 예외 코드
    DUPLICATE_REPORT(HttpStatus.BAD_REQUEST, "REPORT-001", "동일한 신고를 여러번 할 수 없습니다."),
    NOT_FOUND_REPORT(HttpStatus.NOT_FOUND, "REPORT-002", "신고 내역을 찾을 수 없습니다"),

    //댓글 예외 코드
    NOT_FOUND_REPLY(HttpStatus.NOT_FOUND, "REPLY-001", "댓글을 찾을 수 없습니다."),

    // 채팅 예외 코드
    NOT_FOUND_CHAT_ROOM(HttpStatus.NOT_FOUND, "CHAT-001", "채팅방을 찾을 수 없습니다"),
    DUPLICATE_CHAT_ROOM(HttpStatus.CONFLICT, "CHAT-002", "채팅방이 이미 존재합니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
