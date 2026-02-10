package com.ny.archive.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 어떤 에러가 있는지 목록 정리하는 Enum
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 1. ID로 찾았을 때 없는 경우
    BOARD_NOT_FOUND("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    JOURNEY_NOT_FOUND("해당 여행이 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    // 2. 비밀번호가 틀림
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

    // 3. 필수 값 누락
    REQUIRED_FIELD_MISSING("필수 입력 값이 누락되었습니다.", HttpStatus.BAD_REQUEST),

    // 4. 파일 관련
    FILE_UPLOAD_ERROR("파일 업로드 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_NOT_FOUND("해당 파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FILE_DELETE_ERROR("파일 삭제 중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),
    INVALID_FILE_TYPE("지원하지 않는 파일 형식입니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
