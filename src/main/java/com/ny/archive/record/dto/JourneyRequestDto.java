package com.ny.archive.record.dto;

import com.ny.archive.record.domain.Category;
import com.ny.archive.record.domain.Journey;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class JourneyRequestDto {

    @Schema(description = "방문 국가", example = "Japan", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "나라는 필수 입력값입니다")
    private String country;

    @Schema(description = "도시/주", example = "Tokyo", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "도시는 필수 입력값입니다")
    private String state;

    @Schema(description = "여행 후기", example = "맛있는 스시를 많이 먹어서 행복했다", requiredMode =
            Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "후기를 입력해주세요")
    private String review;

    @Schema(description = "평점 (1-5)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "평점은 필수입니다")
    private Integer rate;

    @Schema(description = "여행 카테고리", example = "INTERNATIONAL", requiredMode =
            Schema.RequiredMode.REQUIRED)
    @NotNull(message = "카테고리를 선택해주세요")
    private Category category;

    @Schema(description = "여행 시작 날짜", example = "2026-02-20", requiredMode =
            Schema.RequiredMode.REQUIRED)
    @NotNull(message = "시작 날짜는 필수입니다")
    private LocalDate startDate;

    @Schema(description = "여행 종료 날짜", example = "2026-02-23", requiredMode =
            Schema.RequiredMode.REQUIRED)
    @NotNull(message = "종료 날짜는 필수입니다")
    private LocalDate endDate;

    @Schema(description = "썸네일로 지정할 사진의 인덱스 (0부터 시작)", example = "0")
    private Integer thumbnailIndex;

    public Journey toEntity() {
        return Journey.builder()
                .country(this.country)
                .state(this.state)
                .review(this.review)
                .rate(this.rate)
                .category(this.category)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .build();
    }
}
