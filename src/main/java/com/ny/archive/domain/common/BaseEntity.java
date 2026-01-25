package com.ny.archive.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // 이 엔티티가 언제 생성되고 수정되는지 계속 감시
public abstract class BaseEntity { // 추상 클래스: 이 클래스 상속하는 자식 엔티티에 밑에 필드를 상속해줌
    @CreatedDate
    @Column(updatable = false, precision = 6) // 생성 시간은 update 방지
    private LocalDateTime createdAt;

    @LastModifiedDate // 데이터 수정될 떄 마다 자동으로 시간 갱신
    @Column(precision = 6)
    private LocalDateTime updatedAt;
}
