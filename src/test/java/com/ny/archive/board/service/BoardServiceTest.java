package com.ny.archive.board.service;

import com.ny.archive.board.domain.Board;
import com.ny.archive.board.dto.BoardDeleteRequestDto;
import com.ny.archive.board.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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

    @Test
    void 게시글_삭제_성공_테스트() {
        // Given
        Long id = 1L;
        String password = "123456";

        // 빌더를 사용하여 가짜 Board entity 만들기
        Board board = Board.builder()
                .password(password)
                .build();

        // 테스트를 위해 private 필드에 값을 넣음 (Board entity의 생성자에는 id를 넣지 않았음)
        ReflectionTestUtils.setField(board, "id", id);

        // 삭제 요청을 보낼 가짜 RequestDto 만들기
        BoardDeleteRequestDto requestDto = BoardDeleteRequestDto.builder()
                .password(password)
                .build();

        // 가짜 레포지토리가 1번 아이디를 찾으면 위에서 만든 board 를 뱉어내게 함
        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        // when: 서비스 실행
        Long deleteBoardId = boardService.deleteBoard(id, requestDto);

        // then: 결과 맞는지 검증
        assertAll(
                () -> assertThat(deleteBoardId).isEqualTo(id),
                () -> verify(boardRepository, times(1)).delete(any(Board.class))
        );

    }


}