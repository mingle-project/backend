package com.oreo.mingle.domain.qna.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oreo.mingle.domain.qna.entity.Question;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnswerListResponse {
    private Long questionId;
    private Long galaxyId;
    private String subject;
    private LocalDate date;
    private List<AnswerResponse> answers;

    public static AnswerListResponse from(Question question, List<AnswerResponse> answers) {
        return AnswerListResponse.builder()
                .questionId(question.getId())
                .galaxyId(question.getGalaxy().getId())
                .subject(question.getSubject())
                .date(question.getDate())
                .answers(answers)
                .build();
    }
}