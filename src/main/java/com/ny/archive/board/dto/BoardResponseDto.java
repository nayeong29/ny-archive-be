package com.ny.archive.board.dto;

import com.ny.archive.board.domain.Board;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class BoardResponseDto {
    // 서버가 프론트에게 해당 필드를 보내주게 됨
    // 그냥 한번 보내고 끝나는 값이기 떄문에 값 변할 일 없음: final
    private final Long id;
    private final String author;
    private final String content;
    private final Integer stickerId;
    private final String createdAt;

    // 생성자
    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.author = board.getAuthor();
        this.content = board.getContent();
        this.stickerId = board.getStickerId();
        this.createdAt = board.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("MM/dd"));
    }
}
