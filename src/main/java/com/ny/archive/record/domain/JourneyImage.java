package com.ny.archive.record.domain;

import com.ny.archive.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JourneyImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journey_id")
    private Journey journey;

    @Column(nullable = false)
    @NotNull
    private String fileName;

    @Column(nullable = false)
    @NotNull
    private boolean isThumbnail;

    @Builder
    public JourneyImage(String fileName, boolean isThumbnail) {
        this.fileName = fileName;
        this.isThumbnail = isThumbnail;
    }

    // 부모 연결: 외부 노출 방지를 위해 protected
    protected void setJourney(Journey journey) {
        this.journey = journey;
    }

}
