package com.ny.archive.record.mapper;

import com.ny.archive.record.domain.Journey;
import com.ny.archive.record.dto.JourneyDetailResponseDto;
import com.ny.archive.record.dto.JourneyListResponseDto;
import com.ny.archive.record.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JourneyResponseMapper {

    private final ImageFileService fileService;

    public JourneyDetailResponseDto toDetailDto(Journey journey) {
        List<String> fullImageUrls = journey.getJourneyImages().stream()
                .map(img -> fileService.getFullPath(img.getImageFileName()))
                .toList();

        String fullThumbnailUrl = fileService.getFullPath(journey.getThumbnailUrl());

        return new JourneyDetailResponseDto(journey, fullImageUrls, fullThumbnailUrl);
    }

    public JourneyListResponseDto toListDto(Journey journey) {
        String fullThumbnailUrl = fileService.getFullPath(journey.getThumbnailUrl());
        return new JourneyListResponseDto(journey, fullThumbnailUrl);
    }
}
