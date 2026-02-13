package com.ny.archive.record.service;

import com.ny.archive.common.exception.CustomException;
import com.ny.archive.common.exception.ErrorCode;
import com.ny.archive.record.domain.Journey;
import com.ny.archive.record.domain.JourneyImage;
import com.ny.archive.record.dto.*;
import com.ny.archive.record.mapper.JourneyResponseMapper;
import com.ny.archive.record.repository.JourneyRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
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
                Collections.emptyMap() : multipartFiles.stream()
                .collect(Collectors.toMap(file -> {
                    String name = file.getOriginalFilename();
                    return name.substring(0, name.lastIndexOf("."));
                }, file -> file));

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

    @Transactional
    public JourneyDetailResponseDto updateJourney(Long id, JourneyRequestDto requestDto,
                                                  List<MultipartFile> multipartFiles) {
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

        // --- 사진 수정 로직 ---

        // 1. [기존 데이터 정리] 살려야 할 이미지 키(UUID) 목록을 Set으로 준비
        Set<String> existFileKeys = requestDto.getImageFiles()
                .stream()
                .map(image -> image.getImageKey())
                .collect(Collectors.toSet());

        // 2. [삭제 처리] DB에는 있지만, 요청 DTO(stayKeys)에는 없는 사진들을 찾아 삭제하기

        List<String> filesToDelete = new ArrayList<>(); // 삭제할 파일명들을 담을 리스트
        journey.getJourneyImages().removeIf(image -> {
            if (!existFileKeys.contains(image.getImageFileKey())) {
                filesToDelete.add(image.getImageFileName());
                return true; // 이 조건에 맞으면 removeIf가 true -> DB에서 지움
            }
            return false; // 안맞으면 false -> 안 지움
        });

        // 3. [파일 준비] 새로 업로드된 multipartFiles를 '파일명(uuid)' 기준 Map으로 만들기
        Map<String, MultipartFile> newImageFileMap = ObjectUtils.isEmpty(multipartFiles) ?
                Collections.emptyMap() : multipartFiles.stream()
                .collect(Collectors.toMap(file -> {
                    String name = file.getOriginalFilename();
                    return name.substring(0, name.lastIndexOf("."));
                }, file -> file));


        // 4. [추가 및 유지] 요청 DTO의 images 리스트를 순회하며 작업하기
        for (ImageFileItemDto fileItemDto : requestDto.getImageFiles()) {
            if (ImageType.NEW.equals(fileItemDto.getType())) {
                MultipartFile file = newImageFileMap.get(fileItemDto.getImageKey());

                if (ObjectUtils.isEmpty(file)) {
                    throw new CustomException(ErrorCode.FILE_NOT_FOUND);
                }

                String savedFileName = imageFileService.saveFile(file, fileItemDto.getImageKey());

                JourneyImage journeyImage = JourneyImage.builder()
                        .journey(journey)
                        .imageFileName(savedFileName)
                        .imageFileKey(fileItemDto.getImageKey())
                        .build();

                journey.addJourneyImage(journeyImage);

            }
        }

        // 5. [썸네일 갱신] requestDto.getThumbnailKey()를 기준으로 Journey의 thumbnailUrl 업데이트
        List<JourneyImage> finalImages = journey.getJourneyImages();

        String thumbnailImage = finalImages.stream()
                .filter(image -> requestDto.getThumbnailKey().equals(image.getImageFileKey()))
                .map(image -> image.getImageFileName())
                .findFirst()
                .orElseGet(() -> finalImages.isEmpty() ? "" : finalImages.get(0)
                        .getImageFileName());

        journey.updateThumbnailUrl(thumbnailImage);

        // 6. [진짜 파일 삭제] 모든 DB 로직이 성공했을 때만 물리 파일 삭제 실행
        filesToDelete.forEach(imageFile -> imageFileService.deleteFile(imageFile));

        return journeyResponseMapper.toDetailDto(journey);
    }

    @Transactional
    public Long deleteJourney(Long id) {
        Journey journey = journeyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.JOURNEY_NOT_FOUND));

        journeyRepository.delete(journey);
        return journey.getId();
    }


}
