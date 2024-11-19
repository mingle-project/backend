package com.oreo.mingle.domain.star.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CollectionStar extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_star_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "star_id", nullable = false)
    private Star star;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "galaxy_id", nullable = false)
    private Galaxy galaxy;

    @Column
    private boolean isMain;

    public void setAsMain() {
        this.isMain = true;
    }

    public void unsetAsMain() {
        this.isMain = false;
    }

    @Builder
    public CollectionStar(Galaxy galaxy, Star star, boolean isMain) {
        this.galaxy = galaxy;
        this.star = star;
        this.isMain = isMain;
    }
}
