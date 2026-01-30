package com.ny.archive.board.boardIntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ny.archive.board.domain.Board;
import com.ny.archive.board.dto.BoardDeleteRequestDto;
import com.ny.archive.board.dto.BoardRequestDto;
import com.ny.archive.board.repository.BoardRepository;
import com.ny.archive.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class BoardIntegrationTest {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("방명록 작성부터 조회까지 성공 시나리오")
    void createAndGetBoardTest() throws Exception {
        // 1. Given: 작성할 데이터 준비 (BoardRequestDto 객체 생성)
        BoardRequestDto boardRequestDto = BoardRequestDto.builder()
                .author("나영")
                .content("내용")
                .stickerId(1)
                .password("1234")
                .build();

        // 자바 객체 JSON 문자열 변환 (직렬화)
        String jsonRequest = objectMapper.writeValueAsString(boardRequestDto);

        // 2. When: MockMvc를 이용해 POST /api/boards 요청 보내기
        ResultActions resultActions = mockMvc.perform(
                post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON) // 지금 보내는건 JSON 데이터임을 서버에 알림
                        .content(jsonRequest) // 실제 JSON 데이터를 본문에 담음
        );

        // 3. Then: 응답 상태 코드가 200인지 확인
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("나영"));

        // (2) 실제로 DB에 잘 저장되었는지 Repository로 확인
        List<Board> allBoards = boardRepository.findAll();
        Long savedId = allBoards.get(0).getId();

        assertAll(
                () -> assertThat(allBoards.size()).isEqualTo(1),
                () -> assertThat(allBoards.get(0).getContent()).isEqualTo("내용")
        );

        // 실제로 조회가 되는지 GET 요청을 이어서 보내보기
        mockMvc.perform(get("/api/boards/{id}", savedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedId))
                .andExpect(jsonPath("$.author").value("나영"));
    }

    @Test
    @DisplayName("방명록 수정 성공 시나리오")
    void updateBoardTest() throws Exception {
        // 1. Given: 데이터 만들기
        Board board = Board.builder()
                .author("나영")
                .content("내용")
                .stickerId(1)
                .password("1234")
                .build();

        Board savedBoard = boardRepository.save(board);
        Long savedId = savedBoard.getId();

        // 수정할 내용 담은 RequestDto 만들기
        BoardRequestDto requestDto = BoardRequestDto.builder()
                .author("주연")
                .content("수정")
                .stickerId(2)
                .password("1234")
                .build();

        // 자바 객체 JSON 객체로 만들기
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // 2. When: 수정 요청하는 api 날리기
        ResultActions resultActions = mockMvc.perform(
                put("/api/boards/{id}", savedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
        );

        // 3. Then: 결과 확인 (상태 코드 및 수정 내용)
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("수정"));

        // DB 에서도 바뀌었는지 결과 확인
        Board updatedBoard = boardRepository.findById(savedId).orElseThrow();
        assertThat(updatedBoard.getAuthor()).isEqualTo("주연");
    }

    @Test
    @DisplayName("방명록 수정 실패 시나리오 - 비밀번호 불일치")
    void updateBoardFailTest() throws Exception {
        // 1. Given: 데이터 만들기
        Board board = Board.builder()
                .author("나영")
                .content("내용")
                .stickerId(1)
                .password("1234")
                .build();

        Board savedBoard = boardRepository.save(board);
        Long savedId = savedBoard.getId();

        // 수정할 내용 담은 RequestDto 만들기
        BoardRequestDto requestDto = BoardRequestDto.builder()
                .author("주연")
                .content("수정")
                .stickerId(2)
                .password("123")
                .build();

        // JSON 객체 만들기
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When: 수정 API 호출
        ResultActions resultActions = mockMvc.perform(
                put("/api/boards/{id}", savedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
        );

        // 3. Then: 실패 검증
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_PASSWORD.getMessage()));

        // DB 바뀌지 않음을 확인
        Board notUpdatedBoard = boardRepository.findById(savedId).orElseThrow();
        assertThat(notUpdatedBoard.getContent()).isEqualTo("내용");

    }

    @Test
    @DisplayName("방명록 삭제 성공 시나리오")
    void deleteBoardTest() throws Exception {
        // 1. Given: 데이터 만들기
        Board board = Board.builder()
                .author("나영")
                .content("내용")
                .stickerId(1)
                .password("1234")
                .build();

        Board savedBoard = boardRepository.save(board);
        Long savedId = savedBoard.getId();

        // 삭제 RequestDto 만들기
        BoardDeleteRequestDto deleteRequestDto = BoardDeleteRequestDto.builder()
                .password("1234")
                .build();

        // JSON 으로 변환
        String jsonRequest = objectMapper.writeValueAsString(deleteRequestDto);

        // 2. When: 삭제 api 호출
        ResultActions resultActions = mockMvc.perform(
                delete("/api/boards/{id}", savedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
        );

        // 3. Then: 성공 검증
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(savedId.toString())); // 본문 내용에 접근 (content)

        // DB 에서 사라졌는지 확인
        boolean exists = boardRepository.existsById(savedId);
        assertThat(exists).isFalse();
    }


}
