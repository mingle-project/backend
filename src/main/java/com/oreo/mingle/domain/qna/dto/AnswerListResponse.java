package com.oreo.mingle.domain.qna.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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

    private List<AnswerResponse> answer;


}
