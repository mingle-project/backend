package com.oreo.mingle.global.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageResponse {
    private String message;

    public static MessageResponse from(String message) {
        return MessageResponse.builder()
                .message(message)
                .build();
    }
}
