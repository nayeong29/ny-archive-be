package com.ny.archive.board.controller;

import com.ny.archive.board.dto.BoardDeleteRequestDto;
import com.ny.archive.board.dto.BoardRequestDto;
import com.ny.archive.board.dto.BoardResponseDto;
import com.ny.archive.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor // Service Bean 주입을 위한 생성자 만들기 (Final Only)
@RequestMapping("/api/board")
@Tag(name = "방명록 API", description = "방명록 작성, 조회, 수정, 삭제 기능을 제공합니다.")
public class BoardController {
    private final BoardService boardService;

    @Operation(summary = "방명록 작성")
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@Valid @RequestBody BoardRequestDto requestDto) {
        BoardResponseDto responseDto = boardService.createBoard(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "방명록 조회")
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> getBoard(@Parameter(description = "게시글 ID") @PathVariable Long id) {
        BoardResponseDto responseDto = boardService.getBoard(id);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "방명록 목록 전체 조회")
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> getBoardList() {
        List<BoardResponseDto> responseDtos = boardService.getBoardList();
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(summary = "방명록 수정")
    @PutMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(@Parameter(description = "게시글 ID") @PathVariable Long id,
                                                        @Valid @RequestBody BoardRequestDto requestDto) {
        BoardResponseDto responseDto = boardService.updateBoard(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "방명록 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteBoard(@Parameter(description = "게시글 ID") @PathVariable Long id,
                                            @Valid @RequestBody BoardDeleteRequestDto deleteRequestDto) {
        Long deletedId = boardService.deleteBoard(id, deleteRequestDto);
        return ResponseEntity.ok(deletedId);
    }
}
