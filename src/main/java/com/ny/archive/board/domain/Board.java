package com.ny.archive.board.domain;

import com.ny.archive.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 자바 기본 생성자 생성
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 자동으로 id 지정해줌
    private Long id;

    @Column(nullable = false) // DB 레벨 제약
    @NotNull // 자바 레벨 제약
    private String author;

    @Column(nullable = false)
    @NotNull
    private String content;

    @Column(nullable = false)
    @NotNull
    private Integer stickerId;

    @Column(nullable = false)
    @NotNull
    private String password;

    // 빌더: Service 계층에서 새로운 데이터를 DB에 저장하기 위한 객체 생성시 사용
    @Builder
    public Board(String author, String content, Integer stickerId, String password){
        this.author=author;
        this.content=content;
        this.stickerId=stickerId;
        this.password=password;
    }

    // 수정용 메서드
    public void update(String author, String content, Integer stickerId){
        this.author=author;
        this.content=content;
        this.stickerId=stickerId;
    }

}
