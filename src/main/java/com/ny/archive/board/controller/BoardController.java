package com.ny.archive.board.controller;

import com.ny.archive.board.dto.BoardDeleteRequestDto;
import com.ny.archive.board.dto.BoardRequestDto;
import com.ny.archive.board.dto.BoardResponseDto;
import com.ny.archive.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
@Tag(name = "방명록 API", description = "방명록 작성, 조회, 수정, 삭제 기능을 제공합니다.")
public class BoardController {
    private final BoardService boardService;

    @Operation(summary = "방명록 작성")
    @PostMapping
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto) {
        return boardService.createBoard(requestDto);
    }

    @Operation(summary = "방명록 조회")
    @GetMapping("/{id}")
    public BoardResponseDto getBoard(@Parameter(description = "게시글 ID") @PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @Operation(summary = "방명록 목록 전체 조회")
    @GetMapping
    public List<BoardResponseDto> getBoardList() {
        return boardService.getBoardList();
    }

    @Operation(summary = "방명록 수정")
    @PutMapping("/{id}")
    public BoardResponseDto updateBoard(@Parameter(description = "게시글 ID") @PathVariable Long id,
                                        @RequestBody BoardRequestDto requestDto) {
        return boardService.updateBoard(id, requestDto);
    }

    @Operation(summary = "방명록 삭제")
    @DeleteMapping("/{id}")
    public Long deleteBoard(@Parameter(description = "게시글 ID") @PathVariable Long id,
                            @RequestBody BoardDeleteRequestDto deleteRequestDto) {
        return boardService.deleteBoard(id, deleteRequestDto);
    }
}
