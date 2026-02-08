package com.ny.archive.common.exception;

import com.ny.archive.common.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


// 프로젝트 전체에서 발생하는 예외 감시
@RestControllerAdvice // 프로젝트 전역에서 발생하는 예외를 잡는 관제탑임을 선언
public class GlobalExceptionHandler {

    // 내가 만든 CustomException이 터지면 이 메서드가 가로챔
    @ExceptionHandler(CustomException.class) // 특정 예외를 지정하줌 (CustomException)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException e) {
        // CustomException (e) 안에 있던 ErrorCode에서 메시지를 꺼내서 프론트한테 전송
        return ResponseEntity // 스프링 전용 응답 객체
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
            MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(errorMessage));
    }

    // 그 외 일반적인 에러들 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        System.out.println("알 수 없는 에러 발생: " + e.getMessage());
        return ResponseEntity
                .internalServerError() // HTTP 상태 코드 500 (서버 내부 오류)
                .body(new ErrorResponseDto("관리자에게 문의하세요."));
    }
}
