package com.oreo.mingle.domain.star.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Level {
    MUNZI("먼지"),
    LITTLESTAR("작은별"),
    BIGSTAR("큰별"),
    ADULT("성체");

    private final String value;

    @JsonCreator
    public static Level deserializer(String value) {
        for(Level level : Level.values()){
            if(level.getValue().equals(value)) {
                return level;
            }
        }
        return null;
    }

    @JsonValue
    public String serializer(){
        return value;
    }
}
