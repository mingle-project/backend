package com.oreo.mingle.domain.star.repository;

import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.domain.star.entity.PetStar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetStarRepository extends JpaRepository<PetStar, Long> {
    Optional<PetStar> findByGalaxy(Galaxy galaxy);

}
