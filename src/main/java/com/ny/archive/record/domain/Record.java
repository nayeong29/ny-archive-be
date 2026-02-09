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
public class Record extends BaseEntity {

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
    private String description;

    @Column(nullable = false)
    @NotNull
    private Integer rate;

    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    @NotNull
    private LocalDate date;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true, fetch =
            FetchType.LAZY)
    private List<RecordImage> recordImages = new ArrayList<>();

    @Column(nullable = false)
    @NotNull
    private Double latitude;

    @Column(nullable = false)
    @NotNull
    private Double longitude;

    @Builder
    public Record(String country, String state, String description,
                  Integer rate, Category category, LocalDate date,
                  Double latitude, Double longitude) {
        this.country = country;
        this.state = state;
        this.description = description;
        this.rate = rate;
        this.category = category;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // 연관관계 메서드: RecordImage 추가 메서드 (사진 한 장 추가)
    public void addRecordImage(RecordImage recordImage) {
        this.recordImages.add(recordImage); // 단일 객체 타입이 아니라서 add를 해줘야 함
        recordImage.setRecord(this);
    }

    // 사진 여러개 추가
    public void addRecordImages(List<RecordImage> recordImages) {
        for (RecordImage recordImage : recordImages) {
            this.recordImages.add(recordImage);
        }
    }
}
