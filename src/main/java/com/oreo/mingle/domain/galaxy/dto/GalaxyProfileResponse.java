package com.oreo.mingle.domain.galaxy.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.domain.galaxy.entity.enums.Age;
import com.oreo.mingle.domain.galaxy.entity.enums.Gender;
import com.oreo.mingle.domain.galaxy.entity.enums.Relationship;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GalaxyProfileResponse {
    private Long galaxyId;
    private String code;
    private String name;
    private Gender gender;
    private Age age;
    private Relationship relationship;
    private Boolean isStarted;
    private int cash;
    private int usersCount;

    public static GalaxyProfileResponse from(Galaxy galaxy, int usersCount) {
        return GalaxyProfileResponse.builder()
                .galaxyId(galaxy.getId())
                .code(galaxy.getCode())
                .name(galaxy.getName())
                .gender(galaxy.getGender())
                .age(galaxy.getAge())
                .relationship(galaxy.getRelationship())
                .isStarted(galaxy.getIsStarted())
                .cash(galaxy.getCash())
                .usersCount(usersCount)
                .build();
    }
}
