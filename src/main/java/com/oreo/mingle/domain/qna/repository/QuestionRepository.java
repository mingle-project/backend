package com.oreo.mingle.domain.qna.repository;

import com.oreo.mingle.domain.qna.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
