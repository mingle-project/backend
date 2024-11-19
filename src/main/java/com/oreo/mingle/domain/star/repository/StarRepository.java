package com.oreo.mingle.domain.star.repository;

import com.oreo.mingle.domain.star.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StarRepository extends JpaRepository<Star, Long> {
    @Query(value = "SELECT * FROM star ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Star> findRandomStar();
}
