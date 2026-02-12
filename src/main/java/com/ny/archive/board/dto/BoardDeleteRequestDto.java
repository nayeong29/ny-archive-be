package com.ny.archive.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardDeleteRequestDto {
    @Schema(description = "비밀번호", example = "1234", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
