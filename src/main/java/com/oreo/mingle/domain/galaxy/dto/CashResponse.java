package com.oreo.mingle.domain.galaxy.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CashResponse {
    private Long galaxyId;
    private int cash;

    public static CashResponse from(Galaxy galaxy) {
        return CashResponse.builder()
                .galaxyId(galaxy.getId())
                .cash(galaxy.getCash())
                .build();
    }
}