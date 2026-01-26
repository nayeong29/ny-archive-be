package com.ny.archive.board.dto;

import com.ny.archive.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class BoardRequestDto {

    private String author;
    private String content;
    private Integer stickerId;
    private String password;

    public Board toEntity() {
        return Board.builder()
                .author(this.author)
                .content(this.content)
                .stickerId(this.stickerId)
                .password(this.password)
                .build();
    }
}
