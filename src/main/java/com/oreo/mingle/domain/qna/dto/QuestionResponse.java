package com.oreo.mingle.domain.qna.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oreo.mingle.domain.qna.entity.Question;
import com.oreo.mingle.domain.qna.repository.QuestionRepository;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class QuestionResponse {
    private Long questionId;
    private String subject;
    private LocalDate date;

    public static QuestionResponse from(Question question) {
        return QuestionResponse.builder()
                .questionId(question.getId())
                .subject(question.getSubject())
                .date(question.getDate())
                .build();
    }
}
