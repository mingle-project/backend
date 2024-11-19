package com.oreo.mingle.domain.qna.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.oreo.mingle.domain.galaxy.entity.enums.Gender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionType {
    A("A유형"),
    B("B유형"),
    C("C유형"),
    D("D유형"),
    E("E유형"),
    F("F유형"),
    G("G유형"),
    H("H유형"),
    I("I유형"),
    J("J유형"),
    K("K유형"),
    L("L유형");

    private final String value;

    @JsonCreator
    public static Gender deserializer(String value) {
        for(Gender gender : Gender.values()){
            if(gender.getValue().equals(value)) {
                return gender;
            }
        }
        return null;
    }

    @JsonValue
    public String serializer(){
        return value;
    }
}
