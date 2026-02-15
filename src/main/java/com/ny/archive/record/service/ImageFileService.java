package com.ny.archive.record.service;

import com.ny.archive.common.exception.CustomException;
import com.ny.archive.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageFileService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.base-url}")
    private String s3BaseUrl;

    public String saveFile(MultipartFile file, String imageFileKey) {

        if (file.getOriginalFilename() == null) {
            throw new CustomException(ErrorCode.INVALID_FILE_EXTENSION);
        }

        // 사용자가 올린 파일 이름을 가져옴
        String originalFilename = file.getOriginalFilename();

        // 확장자 존재 확인
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex < 0) {
            throw new CustomException(ErrorCode.INVALID_FILE_EXTENSION);
        }

        // 확장자 추출
        String extension = originalFilename.substring(dotIndex);

        // imageKey + 확장자로 이름 저장
        String savedFilename = "journey/" + imageFileKey + extension;

        try (var inputStream = file.getInputStream()) {
            // S3 업로드 명세서 작성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(savedFilename)
                    .contentType(file.getContentType())
                    .build();

            // S3으로 실제 파일 전송
            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(inputStream, file.getSize()));

            // DB에 저장할 수 있게 새로 만든 파일 이름 리턴
            return savedFilename;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    public void deleteFile(String imageFileName) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(imageFileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FILE_DELETE_ERROR);
        }
    }

    public String getFullPath(String fileName) {
        if (fileName == null) return null;
        return s3BaseUrl + fileName;
    }
}
