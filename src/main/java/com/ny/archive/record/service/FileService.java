package com.ny.archive.record.service;

import com.ny.archive.common.exception.CustomException;
import com.ny.archive.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    private final String uploadPath = "/Users/nellie/backend-dev/images/";

    @Value("${file.prefix}")
    private String prefix;

    public String saveFile(MultipartFile file) {
        try {
            // 사용자가 올린 파일 이름을 가져옴
            String originalFilename = file.getOriginalFilename();

            // 파일명 중복 방지를 위해 유일한 값인 UUID를 이름 앞에 붙여줌
            String savedFilename = UUID.randomUUID().toString() + "_" + originalFilename;

            // 어느 폴더에 어떤 이름으로 저장할지 정하는 File 객체 만듦 (주소지 만들기)
            File target = new File(uploadPath + savedFilename);

            // file 을 target 주소로 넣음
            file.transferTo(target);

            // DB에 저장할 수 있게 새로 만든 파일 이름 리턴
            return savedFilename;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    public void deleteFile(String filename) {
        try {
            // 파일 이름으로 로컬 저장소 경로 찾아냄
            Path path = Paths.get(uploadPath + filename);

            // 파일이 있으면 지우고 없으면 넘어감
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_DELETE_ERROR);
        }
    }

    public String getFullPath(String fileName) {
        if (fileName == null) return null;
        return prefix + fileName;
    }
}
