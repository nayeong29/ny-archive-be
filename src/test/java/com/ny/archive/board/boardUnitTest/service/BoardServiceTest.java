package com.ny.archive.board.boardUnitTest.service;

import com.ny.archive.board.domain.Board;
import com.ny.archive.board.dto.BoardDeleteRequestDto;
import com.ny.archive.board.dto.BoardRequestDto;
import com.ny.archive.board.dto.BoardResponseDto;
import com.ny.archive.board.repository.BoardRepository;
import com.ny.archive.board.service.BoardService;
import com.ny.archive.common.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;

    private Board commonBoard;
    private final Long testId = 1L;

    @BeforeEach
    void init() {
        commonBoard = Board.builder()
                .password("12345")
                .author("나영")
                .content("기본 내용")
                .stickerId(1)
                .build();
        ReflectionTestUtils.setField(commonBoard, "id", testId);
        ReflectionTestUtils.setField(commonBoard, "createdAt", LocalDateTime.now());

    }

    @Test
    @DisplayName("방명록 조회 성공 - 알맞은 ID로 조회 시 방명록이 조회된다")
    void getBoard() {
        // 1. Given
        // 가짜 레포지토리에서 1번 아이디를 찾으면 위에서 만든 board 를 뱉어내게 함
        given(boardRepository.findById(testId)).willReturn(Optional.of(commonBoard));

        // 2. When
        // 서비스가 가짜 레포지토리에서 1번 아이디를 찾기 시작함
        // Given 에서 합의한대로 뱉은 board로 Service 로직을 돌림
        BoardResponseDto result = boardService.getBoard(testId);

        // Then
        assertThat(result.getAuthor()).isEqualTo(commonBoard.getAuthor());
    }

    @Test
    @DisplayName("방명록 조회 실패 - 없는 ID로  조회 시 방명록이 조회된다")
    void getBoardException() {
        // Given
        // 레포가 빈 객체를 뱉도록 함
        given(boardRepository.findById(testId)).willReturn(Optional.empty());

        // When & Then
        // 빈 객체가 들어왔을때 정상적으로 Exception 이 터지는지 확인
        assertThrows(CustomException.class, () -> {
            boardService.getBoard(testId);
        });
    }

    @Test
    @DisplayName("방명록 삭제 성공 - 올바른 비밀번호 입력 시 방명록이 삭제된다")
    void deleteBoard() {
        // 1. Given
        // 삭제 요청을 보낼 가짜 RequestDto 만들기
        BoardDeleteRequestDto requestDto = BoardDeleteRequestDto.builder()
                .password("12345")
                .build();

        // 가짜 레포지토리가 1번 아이디를 찾으면 위에서 만든 board 를 뱉어내게 함
        given(boardRepository.findById(testId)).willReturn(Optional.of(commonBoard));

        // 2. When: 서비스 실행
        Long deleteBoardId = boardService.deleteBoard(testId, requestDto);

        // 3. Then: 결과 맞는지 검증
        assertAll(
                () -> assertThat(deleteBoardId).isEqualTo(testId),
                () -> verify(boardRepository, times(1)).delete(any(Board.class))
        );

    }

    @Test
    @DisplayName("방명록 수정 성공 - 올바른 비밀번호 입력 시 방명록이 수정된다")
    void updateBoard() {
        // 1. Given
        // 수정하는 값으로 날릴 가짜 requestDto 만들기
        BoardRequestDto requestDto = BoardRequestDto.builder()
                .password("12345")
                .author("나뇽")
                .content("수정")
                .stickerId(2)
                .build();

        given(boardRepository.findById(testId)).willReturn(Optional.of(commonBoard));

        // 2. When
        boardService.updateBoard(testId, requestDto);

        // 3. Then
        assertAll(
                () -> assertThat(commonBoard.getContent()).isEqualTo("수정"),
                () -> assertThat(commonBoard.getStickerId()).isEqualTo(2),
                () -> assertThat(commonBoard.getAuthor()).isEqualTo("나뇽")
        );
    }

    @Test
    @DisplayName("방명록 수정 실패 - 틀린 비밀번호 입력 시 Exception 을 출력한다")
    void updateBoardException() {
        // 1. Given
        // 가짜 Dto 만들기
        BoardRequestDto wrongRequestDto = BoardRequestDto.builder()
                .password("123")
                .author("나뇽")
                .content("수정")
                .stickerId(2)
                .build();

        given(boardRepository.findById(testId)).willReturn(Optional.of(commonBoard));

        // 2. When & Then
        // 첫 번째 인자: 어떤 종류의 에러가 터질지 미리 알려줌
        // 두 번째 인자: 에러를 일으킬 로직
        assertThrows(CustomException.class, () -> {
            boardService.updateBoard(testId, wrongRequestDto);
        });
    }
}