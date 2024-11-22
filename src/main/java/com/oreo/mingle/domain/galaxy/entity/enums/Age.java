package com.oreo.mingle.domain.galaxy.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Age {
    AGE_10("10대"),
    AGE_20("20대"),
    AGE_30("30대"),
    AGE_40("40대 이상");

    private final String value;

    @JsonCreator
    public static Age deserializer(String value) {
        for(Age age : Age.values()){
            if(age.getValue().equals(value)) {
                return age;
            }
        }
        return null;
    }

    @JsonValue
    public String serializer(){
        return value;
    }
}