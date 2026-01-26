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
@Transactional(readOnly=true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto){
        Board board = requestDto.toEntity();
        return new BoardResponseDto(boardRepository.save(board));
    }
}
