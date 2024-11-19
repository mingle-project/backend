package com.oreo.mingle.domain.star.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CollectionStarResponse(
        Long id,
        Long galaxyId,
        Long starId,
        String name,
        boolean isMain
) {}
