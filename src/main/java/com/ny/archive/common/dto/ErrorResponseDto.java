package com.ny.archive.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private final String message;
    private final String code;

    public ErrorResponseDto(String message) {
        this.message = message;
        this.code = "COMMON_ERROR";
    }
}
