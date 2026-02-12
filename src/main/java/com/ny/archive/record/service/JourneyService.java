package com.ny.archive.record.service;

import com.ny.archive.common.exception.CustomException;
import com.ny.archive.common.exception.ErrorCode;
import com.ny.archive.record.domain.Journey;
import com.ny.archive.record.domain.JourneyImage;
import com.ny.archive.record.dto.JourneyDetailResponseDto;
import com.ny.archive.record.dto.JourneyRequestDto;
import com.ny.archive.record.repository.JourneyRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JourneyService {

    private final JourneyRepository journeyRepository;
    private final FileService fileService;

    @Transactional
    public JourneyDetailResponseDto createJourney(JourneyRequestDto requestDto,
                                                  List<MultipartFile> multipartFiles) {
        Journey journey = requestDto.toEntity();

        if (!ObjectUtils.isEmpty(multipartFiles)) { // 리스트 존재 검사
            IntStream.range(0, multipartFiles.size()).forEach(i -> {

                MultipartFile multipartFile = multipartFiles.get(i);

                if (multipartFile.isEmpty()) {
                    return; // 파일이 비었는지 검사
                }

                String imageName = fileService.saveFile(multipartFile);

                if (requestDto.getThumbnailIndex() == i) {
                    journey.updateThumbnailUrl(imageName);
                }

                JourneyImage journeyImage = JourneyImage.builder()
                        .fileName(imageName)
                        .build();

                journey.addJourneyImage(journeyImage);
            });
        }

        Journey savedJourney = journeyRepository.save(journey);
        return new JourneyDetailResponseDto(savedJourney);
    }

    @Transactional
    public JourneyDetailResponseDto getJourney(Long id) {
        Journey journey = journeyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.JOURNEY_NOT_FOUND));
        return new JourneyDetailResponseDto(journey);
    }

//    @Transactional
//    public JourneyListResponseDto getJourneyList() {
//        return journeyRepository.findAllByOrderByStartDateDesc()
//                .stream()
//                .map(JourneyListResponseDto::new)
//                .toList();
//    }
//
//    @Transactional
//    public JourneyDetailResponseDto updateJourney(Long id, JourneyRequestDto requestDto) {
//        Journey journey = journeyRepository.findById(id)
//                .orElseThrow(() -> new CustomException(ErrorCode.JOURNEY_NOT_FOUND));
//
//        journey.update(
//                requestDto.getCountry(),
//                requestDto.getState(),
//                requestDto.getReview(),
//                requestDto.getRate(),
//                requestDto.getCategory(),
//                requestDto.getStartDate(),
//                requestDto.getEndDate()
//        );
//
//        return new JourneyDetailResponseDto(journey);
//    }
//
//    @Transactional
//    public Long deleteJourney(Long id) {
//        Journey journey = journeyRepository.findById(id)
//                .orElseThrow(() -> new CustomException(ErrorCode.JOURNEY_NOT_FOUND));
//
//        journeyRepository.delete(journey);
//        return journey.getId();
//    }


}
