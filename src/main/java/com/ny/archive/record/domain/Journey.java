package com.ny.archive.record.domain;

import com.ny.archive.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Journey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String country;

    @Column(nullable = false)
    @NotNull
    private String state;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotNull
    private String review;

    @Column(nullable = false)
    @NotNull
    private Integer rate;

    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    @NotNull
    private LocalDate startDate;

    @Column(nullable = false)
    @NotNull
    private LocalDate endDate;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true, fetch =
            FetchType.LAZY)
    private List<JourneyImage> journeyImages = new ArrayList<>();


    @Builder
    public Journey(String country,
                   String state,
                   String review,
                   Integer rate,
                   Category category,
                   LocalDate startDate,
                   LocalDate endDate) {
        this.country = country;
        this.state = state;
        this.review = review;
        this.rate = rate;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // 연관관계 메서드: RecordImage 추가 메서드 (사진 한 장 추가)
    public void addRecordImage(JourneyImage journeyImage) {
        this.journeyImages.add(journeyImage); // 단일 객체 타입이 아니라서 add를 해줘야 함
        journeyImage.setJourney(this);
    }

    // 사진 여러개 추가
    public void addRecordImages(List<JourneyImage> journeyImages) {
        for (JourneyImage journeyImage : journeyImages) {
            this.addRecordImage(journeyImage);
        }
    }
}
