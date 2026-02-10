package com.ny.archive.record.dto;

import com.ny.archive.record.domain.Category;
import com.ny.archive.record.domain.Journey;
import com.ny.archive.record.domain.JourneyImage;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class JourneyResponseDto {

    private final Long id;
    private final String country;
    private final String state;
    private final String review;
    private final Integer rate;
    private final Category category;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<JourneyImage> journeyImages = new ArrayList<>();
    private final String createdAt;

    // 생성자
    public JourneyResponseDto(Journey journey) {
        this.id = journey.getId();
        this.country = journey.getCountry();
        this.state = journey.getState();
        this.review = journey.getReview();
        this.rate = journey.getRate();
        this.category = journey.getCategory();
        this.startDate = journey.getStartDate();
        this.endDate = journey.getEndDate();
        this.createdAt = journey.getCreatedAt().format(DateTimeFormatter.ofPattern("yy/MM/dd"));
    }
}
