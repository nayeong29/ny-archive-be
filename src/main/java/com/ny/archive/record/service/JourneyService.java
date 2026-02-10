package com.ny.archive.record.service;

import com.ny.archive.common.exception.CustomException;
import com.ny.archive.common.exception.ErrorCode;
import com.ny.archive.record.domain.Journey;
import com.ny.archive.record.dto.JourneyRequestDto;
import com.ny.archive.record.repository.JourneyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JourneyService {

    private final JourneyRepository journeyRepository;

    @Transactional
    public JourneyResponseDto createJourney(JourneyRequestDto requestDto) {
        Journey journey = requestDto.toEntity();
        return new JourneyResponseDto(journeyRepository.save(journey));
    }

    @Transactional
    public JourneyResponseDto getJourney(Long id) {
        Journey journey = journeyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.JOURNEY_NOT_FOUND));
        return new JourneyResponseDto(journey);
    }

    @Transactional
    public List<JourneyResponseDto> getJourneyList() {
        return journeyRepository.findAllByOrderByStartDateDesc()
                .stream()
                .map(JourneyResponseDto::new)
                .toList();
    }

    @Transactional
    public JourneyResponseDto updateJourney(Long id, JourneyRequestDto requestDto) {
        Journey journey = journeyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.JOURNEY_NOT_FOUND));

        journey.update(
                requestDto.getCountry(),
                requestDto.getState(),
                requestDto.getReview(),
                requestDto.getRate(),
                requestDto.getCategory(),
                requestDto.getStartDate(),
                requestDto.getEndDate()
        );

        return new JourneyResponseDto(journey);
    }

    @Transactional
    public Long deleteJourney(Long id) {
        Journey journey = journeyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.JOURNEY_NOT_FOUND));

        journeyRepository.delete(journey);
        return journey.getId();
    }


}
