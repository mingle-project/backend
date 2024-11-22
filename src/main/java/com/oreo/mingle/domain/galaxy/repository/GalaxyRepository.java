package com.oreo.mingle.domain.galaxy.repository;

import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GalaxyRepository extends JpaRepository<Galaxy, Long> {
    Optional<Galaxy> findByCode(String code);
}