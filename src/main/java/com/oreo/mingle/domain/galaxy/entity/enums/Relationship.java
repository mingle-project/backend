package com.oreo.mingle.domain.galaxy.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Relationship {
    ACQUAINTANCE("알아가는 사이"),
    CLOSE("비밀 없는 사이"),
    COMFORTABLE("편한 사이");

    private final String value;

    @JsonCreator
    public static Relationship deserializer(String value) {
        for(Relationship relationship : Relationship.values()){
            if(relationship.getValue().equals(value)) {
                return relationship;
            }
        }
        return null;
    }

    @JsonValue
    public String serializer(){
        return value;
    }
}
