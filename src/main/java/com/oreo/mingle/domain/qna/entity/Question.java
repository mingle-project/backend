package com.oreo.mingle.domain.qna.entity;

import com.oreo.mingle.domain.qna.entity.enums.QuestionType;
import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private QuestionType type;
  
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "galaxy_id", nullable = false)
    private Galaxy galaxy;

    @Column(length = 200)
    private String subject;

    public LocalDate date;
}