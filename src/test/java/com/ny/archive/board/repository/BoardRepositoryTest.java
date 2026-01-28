package com.ny.archive.board.repository;

import com.ny.archive.board.domain.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

// JPA 테스트용 어노테이션
// 테스트가 끝나면 자동으로 롤백
@DataJpaTest
class BoardRepositoryTest {

    @Autowired // BoardRepository 빈 주입
    private BoardRepository boardRepository;

    @Test
    @DisplayName("최신순 정렬 조회 테스트 - 방명록이 생성일 내림차순으로 조회되어야 한다")
    void findAllByOrderByCreatedAtDesc() {
        // 1. Given: 시간차를 두고 방명록 2개 저장
        Board board1 = Board.builder()
                .author("나영")
                .content("내용")
                .stickerId(1)
                .password("1234")
                .build();

        Board board2 = Board.builder()
                .author("주연")
                .content("하하")
                .stickerId(2)
                .password("5678")
                .build();

        boardRepository.save(board1);
        boardRepository.save(board2);

        // 2. When
        List<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc();

        // 3. Then
        assertAll(
                () -> assertThat(boards.get(0).getAuthor()).isEqualTo("주연"),
                () -> assertThat(boards.size()).isEqualTo(2)
        );
    }
}