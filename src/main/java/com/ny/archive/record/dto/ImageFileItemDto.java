package com.ny.archive.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class ImageFileItemDto {

    @Schema(description = "이미지 고유 키", example = "uuid-123")
    private String imageKey;

    @Schema(description = "이미지 상태 (NEW / EXISTING)", example = "NEW")
    private ImageType type;
}
