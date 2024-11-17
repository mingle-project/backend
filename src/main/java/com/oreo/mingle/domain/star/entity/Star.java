package com.oreo.mingle.domain.star.entity;

import com.oreo.mingle.domain.star.entity.enums.Rarity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "star_id")
    private Long id;

    private String name;

    private String image;

    private String color;

    @Enumerated(EnumType.STRING)
    private Rarity rarity;
}