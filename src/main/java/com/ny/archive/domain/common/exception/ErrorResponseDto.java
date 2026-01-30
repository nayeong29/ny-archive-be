package com.ny.archive.domain.common.exception;

import lombok.Getter;

@Getter
public class ErrorResponseDto {
    private final String message;

    public ErrorResponseDto(String message) {
        this.message = message;
    }
}
