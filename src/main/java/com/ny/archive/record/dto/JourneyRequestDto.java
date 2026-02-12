package com.ny.archive.record.dto;

import com.ny.archive.record.domain.Category;
import com.ny.archive.record.domain.Journey;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class JourneyRequestDto {

    @Schema(description = "방문 국가", example = "Japan")
    @NotBlank(message = "나라는 필수 입력값입니다")
    private String country;

    @Schema(description = "도시/주", example = "Tokyo")
    @NotBlank(message = "도시는 필수 입력값입니다")
    private String state;

    @Schema(description = "여행 후기", example = "맛있는 스시를 많이 먹어서 행복했다")
    @NotBlank(message = "후기를 입력해주세요")
    private String review;

    @Schema(description = "평점 (1-5)", example = "5")
    @NotNull(message = "평점은 필수입니다")
    private Integer rate;

    @Schema(description = "여행 카테고리", example = "INTERNATIONAL")
    @NotNull(message = "카테고리를 선택해주세요")
    private Category category;

    @Schema(description = "여행 시작 날짜", example = "2026-02-20")
    @NotNull(message = "시작 날짜는 필수입니다")
    private LocalDate startDate;

    @Schema(description = "여행 종료 날짜", example = "2026-02-23")
    @NotNull(message = "종료 날짜는 필수입니다")
    private LocalDate endDate;

    @Schema(description = "이미지 파일 상세 정보 리스트")
    @Builder.Default
    private List<ImageFileItemDto> imageFiles = new ArrayList<>();

    @Schema(description = "썸네일로 지정할 이미지의 고유 키", example = "uuid-123")
    private String thumbnailKey;

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
