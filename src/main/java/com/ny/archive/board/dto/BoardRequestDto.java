package com.ny.archive.board.dto;

import com.ny.archive.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter // 테스트 코드 짤 때 빌더 필요
public class BoardRequestDto {
    // 서버가 프론트에서 해당 필드를 요구하게 됨
    private String author;
    private String content;
    private Integer stickerId;
    private String password;

    // 엔티티 조립용
    public Board toEntity() {
        return Board.builder()
                .author(this.author)
                .content(this.content)
                .stickerId(this.stickerId)
                .password(this.password)
                .build();
    }
}
