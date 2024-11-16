package com.oreo.mingle.domain.qna.repository;

import com.oreo.mingle.domain.qna.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
