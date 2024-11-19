package com.oreo.mingle.domain.galaxy.entity;

import com.oreo.mingle.domain.galaxy.entity.enums.Age;
import com.oreo.mingle.domain.galaxy.entity.enums.Gender;
import com.oreo.mingle.domain.galaxy.entity.enums.Relationship;
import com.oreo.mingle.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Galaxy extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "galaxy_id")
    private Long id;

    @Column(unique = true)
    private String code;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Age age;

    @Enumerated(EnumType.STRING)
    private Relationship relationship;

    @Builder.Default
    private Boolean isStarted = false;

    @Builder.Default
    private int cash = 100;

    public void updateName(String name) {
        this.name = name;
    }

    public void updateOptions(Gender gender, Age age, Relationship relationship) {
        this.gender = gender;
        this.age = age;
        this.relationship = relationship;
    }
}