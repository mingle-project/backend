package com.oreo.mingle.domain.star.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Rarity {
    COMMON("일반"),
    RARE("희귀"),
    EPIC("에픽"),
    LEGENDARY("전설");

    private final String value;

    @JsonCreator
    public static Rarity deserializer(String value) {
        for(Rarity rarity : Rarity.values()){
            if(rarity.getValue().equals(value)) {
                return rarity;
            }
        }
        return null;
    }

    @JsonValue
    public String serializer(){
        return value;
    }
}
