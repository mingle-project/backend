package com.oreo.mingle.domain.galaxy.dto;

import com.oreo.mingle.domain.galaxy.entity.enums.Age;
import com.oreo.mingle.domain.galaxy.entity.enums.Gender;
import com.oreo.mingle.domain.galaxy.entity.enums.Relationship;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateGalaxyRequest {
    private String name;
    private Gender gender;
    private Age age;
    private Relationship relationship;
}
