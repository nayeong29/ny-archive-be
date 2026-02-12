package com.ny.archive.record.service;

import com.ny.archive.common.exception.CustomException;
import com.ny.archive.common.exception.ErrorCode;
import com.ny.archive.record.domain.Journey;
import com.ny.archive.record.domain.JourneyImage;
import com.ny.archive.record.dto.ImageFileItemDto;
import com.ny.archive.record.dto.JourneyDetailResponseDto;
import com.ny.archive.record.dto.JourneyListResponseDto;
import com.ny.archive.record.dto.JourneyRequestDto;
import com.ny.archive.record.mapper.JourneyResponseMapper;
import com.ny.archive.record.repository.JourneyRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JourneyService {

    private final JourneyRepository journeyRepository;
    private final ImageFileService imageFileService;
    private final JourneyResponseMapper journeyResponseMapper;

    @Transactional
    public JourneyDetailResponseDto createJourney(JourneyRequestDto requestDto,
                                                  List<MultipartFile> multipartFiles) {
        // 1. 여행 객체 생성
        Journey journey = requestDto.toEntity();

        // 2. 파일 리스트 <키, 파일> 기준 Map 변환
        Map<String, MultipartFile> imageFileMap = ObjectUtils.isEmpty(multipartFiles) ?
                Collections.emptyMap() :
                multipartFiles.stream().collect(Collectors.toMap
                        (file -> {
                             String name = file.getOriginalFilename();
                             return name.substring(0, name.lastIndexOf("."));
                         },
                         file -> file
                        ));

        // 3. RequestDto 에서 필요한 값 꺼내서 JourneyImage 객체 생성 및 저장
        if (!ObjectUtils.isEmpty(requestDto.getImageFiles())) {
            for (ImageFileItemDto fileItemDto : requestDto.getImageFiles()) {
                MultipartFile imageFile = imageFileMap.get(fileItemDto.getImageKey());

                if (ObjectUtils.isEmpty(imageFile)) {
                    throw new CustomException(ErrorCode.FILE_NOT_FOUND);
                }

                // fileService 호출해서 저장될 이름으로 변경
                String savedFileName = imageFileService.saveFile(imageFile,
                                                                 fileItemDto.getImageKey());

                // 썸네일 없는 경우에는 제일 첫번째로 루프 도는 savedFile이 썸네일
                if (ObjectUtils.isEmpty(journey.getThumbnailUrl())) {
                    journey.updateThumbnailUrl(savedFileName);
                }

                // 돌다가 진자 썸네일 찾으면 썸네일 설정
                if (fileItemDto.getImageKey().equals(requestDto.getThumbnailKey())) {
                    journey.updateThumbnailUrl(savedFileName);
                }

                // JourneyImage 객체 생성
                JourneyImage journeyImage = JourneyImage.builder()
                        .journey(journey)
                        .imageFileName(savedFileName)
                        .imageFileKey(fileItemDto.getImageKey())
                        .build();

                // 생성한 객체 journey에 연결
                journey.addJourneyImage(journeyImage);
            }
        }

        // 4. toDetailDto로 파일 prefix + fileName 형태로 변경해서 return
        return journeyResponseMapper.toDetailDto(journeyRepository.save(journey));
    }

    @Transactional
    public JourneyDetailResponseDto getJourney(Long id) {
        Journey journey = journeyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.JOURNEY_NOT_FOUND));

        return journeyResponseMapper.toDetailDto(journey);
    }

    @Transactional
    public List<JourneyListResponseDto> getJourneyList() {
        return journeyRepository.findAllByOrderByStartDateDesc()
                .stream()
                .map(journey -> journeyResponseMapper.toListDto(journey))
                .toList();
    }
//
//    @Transactional
//    public JourneyDetailResponseDto updateJourney(Long id, JourneyUpdateRequestDto requestDto,
//    List<MultipartFile> multipartFiles) {
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
//        return journeyResponseMapper.toDetailDto(journey)
//    }

//    @Transactional
//    public Long deleteJourney(Long id) {
//        Journey journey = journeyRepository.findById(id)
//                .orElseThrow(() -> new CustomException(ErrorCode.JOURNEY_NOT_FOUND));
//
//        journeyRepository.delete(journey);
//        return journey.getId();
//    }


}
