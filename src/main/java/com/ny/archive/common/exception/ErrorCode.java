package com.ny.archive.common.exception;

import lombok.Getter;

// 어떤 에러가 있는지 목록 정리하는 Enum
@Getter
public enum ErrorCode {
    // 1. ID로 찾았을 때 해당 게시글이 없는 경우
    BOARD_NOT_FOUND("해당 게시글이 존재하지 않습니다."),

    // 2. 수정/삭제 시 비밀번호가 틀린 경우
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),

    // 3. 필수 입력값(작성자, 내용 등)이 비어있는 경우
    REQUIRED_FIELD_MISSING("필수 입력 값이 누락되었습니다.");

    private final String message;

    // 에러코드에 따라 그 뒤의 메시지를 저장해둠
    ErrorCode(String message) {
        this.message = message;
    }
}
