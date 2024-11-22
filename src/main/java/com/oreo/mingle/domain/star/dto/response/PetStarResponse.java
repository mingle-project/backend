package com.oreo.mingle.domain.star.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oreo.mingle.domain.star.entity.enums.Rarity;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PetStarResponse(
        Long PetId,
        Long galaxyId,
        Long starId,

        String name,
        String color,
        String image,
        Rarity Rarity,

        int level,
        int point
) {}
