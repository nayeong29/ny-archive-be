package com.ny.archive.board.dto;

import com.ny.archive.board.domain.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class BoardResponseDto {
    private Long id;
    private String author;
    private String content;
    private Integer stickerId;
    private String createdAt;

    public BoardResponseDto (Board board){
        this.id= board.getId();
        this.author= board.getAuthor();
        this.content= board.getContent();
        this.stickerId= board.getStickerId();
        this.createdAt= board.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"));
    }
}
