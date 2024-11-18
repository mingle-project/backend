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
    @JoinColumn(name = "galaxy_id", referencedColumnName = "galaxy_id")
    private Galaxy galaxy;

    @Column(length = 200)
    private String subject; // 질문 제목

    @Column(columnDefinition = "TEXT", name = "content")
    private String content; // 내용


    @Column(name = "date")
    public LocalDate date; // 질문 시간

//    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE) //질문 삭제시 달린 답변 삭제
//    private List<Answer> answerList;
}