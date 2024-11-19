package com.oreo.mingle.domain.star.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PetStar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_star_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "star_id", nullable = false)
    private Star star;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "galaxy_id", nullable = false)
    private Galaxy galaxy;

    @Column
    private int level;

    @Column
    private int point;

    public void changeStar(Star newStar) {
        this.star = newStar;
    }

    public void resetLevelAndPoints() {
        this.level = 1;
        this.point = 0;
    }

}
