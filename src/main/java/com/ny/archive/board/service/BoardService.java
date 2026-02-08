package com.ny.archive.board.service;

import com.ny.archive.board.domain.Board;
import com.ny.archive.board.dto.BoardDeleteRequestDto;
import com.ny.archive.board.dto.BoardRequestDto;
import com.ny.archive.board.dto.BoardResponseDto;
import com.ny.archive.board.repository.BoardRepository;
import com.ny.archive.common.exception.CustomException;
import com.ny.archive.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // 레포 Bean 주입을 위해 생성자 만들어줌 (Final Only)
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        Board board = requestDto.toEntity();
        return new BoardResponseDto(boardRepository.save(board));
    }

    @Transactional
    public BoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        return new BoardResponseDto(board);
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
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        if (!board.getPassword().equals(requestDto.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // @Transactional -> 변경 사항 있으면 자동으로 update 함 (save 또 안써도 됨)
        board.update(
                requestDto.getAuthor(),
                requestDto.getContent(),
                requestDto.getStickerId()
        );

        return new BoardResponseDto(board);

    }

    @Transactional
    public Long deleteBoard(Long id, BoardDeleteRequestDto deleteRequestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        if (!board.getPassword().equals(deleteRequestDto.getPassword())) {
            throw new CustomException((ErrorCode.INVALID_PASSWORD));
        }

        boardRepository.delete(board);
        return board.getId();
    }
}
