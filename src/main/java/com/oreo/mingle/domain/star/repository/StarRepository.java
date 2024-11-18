package com.oreo.mingle.domain.star.repository;

import com.oreo.mingle.domain.star.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarRepository extends JpaRepository<Star, Long> {
}