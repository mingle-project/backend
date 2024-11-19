package com.oreo.mingle.domain.star.repository;

import com.oreo.mingle.domain.star.entity.CollectionStar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionStarRepository extends JpaRepository<CollectionStar, Long> {
    List<CollectionStar> findByGalaxyId(Long galaxy_id);
    Optional<CollectionStar> findByIsMainTrueAndGalaxyId(Long galaxy_id);
    Optional<CollectionStar> findByGalaxyIdAndStarId(Long galaxyId, Long starId);
}