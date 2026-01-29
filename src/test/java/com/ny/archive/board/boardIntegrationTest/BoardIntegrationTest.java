package com.ny.archive.board.boardIntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ny.archive.board.domain.Board;
import com.ny.archive.board.dto.BoardRequestDto;
import com.ny.archive.board.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
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
                        .content(jsonRequest)
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
}
