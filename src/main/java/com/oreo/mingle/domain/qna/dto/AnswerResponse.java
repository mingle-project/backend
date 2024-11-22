package com.oreo.mingle.domain.qna.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oreo.mingle.domain.qna.entity.Answer;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnswerResponse {
    private Long answerId;
    private Long userId;
    private String ninkname;
    private String content;
    private String message;

    public static AnswerResponse from(Answer answer) {
        return AnswerResponse.builder()
                .answerId(answer.getId())
                .userId(answer.getUser().getId())
                .ninkname(answer.getUser().getNickname())
                .content(answer.getContent())
                .build();
    }
}
