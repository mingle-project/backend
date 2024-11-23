package com.oreo.mingle.domain.star.repository;

import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.domain.star.entity.CollectionStar;
import com.oreo.mingle.domain.star.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionStarRepository extends JpaRepository<CollectionStar, Long> {
    List<CollectionStar> findByGalaxy(Galaxy galaxy);
    Optional<CollectionStar> findByIsMainTrueAndGalaxy(Galaxy galaxy);
    Optional<CollectionStar> findByGalaxyAndStar(Galaxy galaxy, Star star);
}