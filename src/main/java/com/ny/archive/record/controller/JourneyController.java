package com.ny.archive.record.controller;

import com.ny.archive.record.service.JourneyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/journey")
@Tag(name = "여행 API", description = "여행 작성, 조회, 수정, 삭제 기능을 제공합니다.")
public class JourneyController {
    private final JourneyService journeyService;

    public JourneyListResponseDto getJourneyList(@RequestBody JourneyRequestDto requestDto,
                                                 List<MultipartFile> multipartFiles) {
        journeyService.getJourneyList();
    }
}
