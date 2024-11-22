package com.oreo.mingle.domain.qna.repository;

import com.oreo.mingle.domain.qna.entity.Answer;
import com.oreo.mingle.domain.qna.entity.Question;
import com.oreo.mingle.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestion(Question question);

    int countByQuestion(Question question);

    Optional<Answer> findByUserAndQuestion(User user, Question question);
}
