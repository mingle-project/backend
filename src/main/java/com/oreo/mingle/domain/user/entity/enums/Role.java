package com.oreo.mingle.domain.user.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("운영자"),
    USER("사용자");

    private final String value;

    @JsonCreator
    public static Role deserializer(String value) {
        for(Role role : Role.values()){
            if(role.getValue().equals(value)) {
                return role;
            }
        }
        return null;
    }

    @JsonValue
    public String serializer(){
        return value;
    }
}