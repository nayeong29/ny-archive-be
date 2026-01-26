package com.ny.archive.board.repository;

import com.ny.archive.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Board 가 엔티티 클래스며 그 엔티티의 ID 타입은 Long이다
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByCreatedAtDesc();
}
