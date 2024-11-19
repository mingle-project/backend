package com.oreo.mingle.domain.galaxy.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GalaxyResponse {
    private Long galaxyId;
    private String name;
    private String message;

    public static GalaxyResponse from(Galaxy galaxy, String message) {
        return GalaxyResponse.builder()
                .galaxyId(galaxy.getId())
                .name(galaxy.getName())
                .message(message)
                .build();
    }
}
