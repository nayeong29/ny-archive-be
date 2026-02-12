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
    private String imageFileName;

    @Column(nullable = false, unique = true)
    private String imageFileKey;

    @Builder
    public JourneyImage(Journey journey, String imageFileName, String imageFileKey) {
        this.journey = journey;
        this.imageFileName = imageFileName;
        this.imageFileKey = imageFileKey;
    }

}
