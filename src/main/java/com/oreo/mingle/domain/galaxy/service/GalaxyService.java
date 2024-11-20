package com.oreo.mingle.domain.galaxy.service;

import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.domain.galaxy.repository.GalaxyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GalaxyService {
    private final GalaxyRepository galaxyRepository;

    public Galaxy findGalaxyById(Long galaxyId) {
        return galaxyRepository.findById(galaxyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 Galaxy를 찾을 수 없습니다."));
    }
}
