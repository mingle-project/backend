package com.oreo.mingle.domain.galaxy.service;

import com.oreo.mingle.domain.galaxy.dto.CreateGalaxyRequest;
import com.oreo.mingle.domain.galaxy.dto.GalaxyResponse;
import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.domain.galaxy.repository.GalaxyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GalaxyService {
    private final GalaxyRepository galaxyRepository;

    public GalaxyResponse createGalaxy(CreateGalaxyRequest request) {
        String code = generateGroupCode();
        Galaxy newGalaxy = Galaxy.builder()
                .name(request.getName())
                .code(code)
                .gender(request.getGender())
                .age(request.getAge())
                .relationship(request.getRelationship())
                .build();
        galaxyRepository.save(newGalaxy);
        return GalaxyResponse.from(newGalaxy, "그룹이 성공적으로 생성되었습니다.");
    }

    public GalaxyResponse joinGalaxyByCode(String code) {
        Galaxy galaxy = galaxyRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("해당 코드를 가진 Galaxy를 찾을 수 없습니다."));
        return GalaxyResponse.from(galaxy, "그룹에 성공적으로 참여하였습니다.");
    }

    private String generateGroupCode() {
        String code = UUID.randomUUID().toString().substring(0, 8);
        if (galaxyRepository.findByCode(code).isPresent()) {
            return generateGroupCode();
        }
        return code;
    }
}