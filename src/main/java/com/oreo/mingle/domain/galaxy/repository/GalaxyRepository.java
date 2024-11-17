package com.oreo.mingle.domain.galaxy.repository;

import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalaxyRepository extends JpaRepository<Galaxy, Long> {
}
