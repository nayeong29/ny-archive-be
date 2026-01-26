package com.ny.archive.board.controller;

import com.ny.archive.board.dto.BoardRequestDto;
import com.ny.archive.board.dto.BoardResponseDto;
import com.ny.archive.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/")
public class BoardController {
    private final BoardService boardService;

    @PatchMapping("/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id,
                                        @RequestBody BoardRequestDto requestDto) {
        return boardService.updateBoard(id, requestDto);
    }

}
