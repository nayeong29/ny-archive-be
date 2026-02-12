package com.ny.archive.record.dto;

import com.ny.archive.record.domain.Category;
import com.ny.archive.record.domain.Journey;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class JourneyDetailResponseDto {

    private final Long id;
    private final String country;
    private final String state;
    private final String review;
    private final Integer rate;
    private final Category category;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String createdAt;
    private final List<String> imageUrls;
    private final String thumbnailUrl;


    public JourneyDetailResponseDto(Journey journey,
                                    List<String> fullImageUrls,
                                    String fullThumbnailUrl) {
        this.id = journey.getId();
        this.country = journey.getCountry();
        this.state = journey.getState();
        this.review = journey.getReview();
        this.rate = journey.getRate();
        this.category = journey.getCategory();
        this.startDate = journey.getStartDate();
        this.endDate = journey.getEndDate();
        this.createdAt = journey.getCreatedAt().format(DateTimeFormatter.ofPattern("yy/MM/dd"));
        this.imageUrls = fullImageUrls;
        this.thumbnailUrl = fullThumbnailUrl;
    }
}
