package com.ny.archive.record.dto;

import com.ny.archive.record.domain.Category;
import com.ny.archive.record.domain.Journey;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class JourneyListResponseDto {
    private final Long id;
    private final String country;
    private final String state;
    private final Integer rate;
    private final Category category;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String createdAt;

    private final String thumbnailUrl;

    public JourneyListResponseDto(Journey journey) {
        this.id = journey.getId();
        this.country = journey.getCountry();
        this.state = journey.getState();
        this.rate = journey.getRate();
        this.category = journey.getCategory();
        this.startDate = journey.getStartDate();
        this.endDate = journey.getEndDate();
        this.createdAt = journey.getCreatedAt().format(DateTimeFormatter.ofPattern("yy/MM/dd"));

        this.thumbnailUrl = journey.getJourneyImages().stream()
                .filter(image -> image.isThumbnail()) // 썸네일 true인 객체 하나만 찾기
                .findFirst() // 찾음
                .map(image -> image.getFullImagePath()) // 그 하나의 객체를 이미지 경로 붙여서 변환하기
                .orElse(null);
    }
}
