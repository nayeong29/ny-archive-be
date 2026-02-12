package com.ny.archive.record.controller;

import com.ny.archive.record.dto.JourneyDetailResponseDto;
import com.ny.archive.record.dto.JourneyListResponseDto;
import com.ny.archive.record.dto.JourneyRequestDto;
import com.ny.archive.record.service.JourneyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/journey")
@Tag(name = "여행 API", description = "여행 작성, 조회, 수정, 삭제 기능을 제공합니다.")
public class JourneyController {
    private final JourneyService journeyService;

    @Operation(summary = "여행 작성")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // 서버가 받아야 할 데이터가 2개라서 각각 이름을 붙여서 프론트에 요청
    public ResponseEntity<JourneyDetailResponseDto> createJourney(@RequestPart("requestDto") @Valid JourneyRequestDto requestDto,
                                                                  @RequestPart(value = "images",
                                                                          required = false) List<MultipartFile> images
    ) {
        JourneyDetailResponseDto responseDto = journeyService.createJourney(requestDto, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "특정 여행 조회")
    @GetMapping("/{id}")
    public ResponseEntity<JourneyDetailResponseDto> getJourney(@PathVariable Long id) {
        JourneyDetailResponseDto responseDto = journeyService.getJourney(id);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "여행 목록 조회")
    @GetMapping
    public ResponseEntity<List<JourneyListResponseDto>> getJourneyList() {
        List<JourneyListResponseDto> responseDto = journeyService.getJourneyList();
        return ResponseEntity.ok(responseDto);
    }
}
