package com.oreo.mingle.domain.qna.repository;

import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.domain.qna.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByGalaxyAndDate(Galaxy galaxy, LocalDate date);

    List<Question> findByGalaxyId(Long galaxyId);

    @Query("SELECT q FROM Question q WHERE q.galaxy.id = :galaxyId ORDER BY q.date DESC")
    Optional<Question> findLatestQuestionByGalaxy(@Param("galaxyId") Long galaxyId);

    int countByGalaxy(Galaxy galaxy);
}
