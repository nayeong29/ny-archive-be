package com.ny.archive.board.dto;

import com.ny.archive.board.domain.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor // 기본 생성자
@Getter // private 값 꺼내오기
@Builder // 테스트 코드
@AllArgsConstructor // Builder를 위해 모든 필드 받는 생성자 만들어주기
public class BoardRequestDto {
    // 서버가 프론트에서 해당 필드를 요구하게 됨
    @Schema(description = "작성자 이름", example = "나영", requiredMode = Schema.RequiredMode.REQUIRED)
    private String author;

    @Schema(description = "방명록 내용", example = "오늘 하루도 화이팅!", requiredMode =
            Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "스티커 아이디", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer stickerId;

    @Schema(description = "비밀번호", example = "1234", requiredMode = Schema.RequiredMode.REQUIRED)
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
