package com.ny.archive.board.service;

import com.ny.archive.board.domain.Board;
import com.ny.archive.board.dto.BoardRequestDto;
import com.ny.archive.board.dto.BoardResponseDto;
import com.ny.archive.board.repository.BoardRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // 레포 주입을 위해 필요함
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        Board board = requestDto.toEntity();
        return new BoardResponseDto(boardRepository.save(board));
    }

    @Transactional
    public List<BoardResponseDto> getBoardList() {
        return boardRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(BoardResponseDto::new)
                .toList();
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        if (!board.getPassword().equals(requestDto.getPassword())) {
            throw new RuntimeException("수정 못함");
        }

        // @Transactional -> 변경 사항 있으면 자동으로 update 함 (save 또 안써도 됨)
        board.update(
                requestDto.getAuthor(),
                requestDto.getContent(),
                requestDto.getStickerId()
        );

        return new BoardResponseDto(board);

    }
}
