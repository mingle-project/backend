package com.oreo.mingle.domain.galaxy.service;

import com.oreo.mingle.domain.galaxy.dto.*;
import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.domain.galaxy.entity.enums.Age;
import com.oreo.mingle.domain.galaxy.entity.enums.Gender;
import com.oreo.mingle.domain.galaxy.repository.GalaxyRepository;
import com.oreo.mingle.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GalaxyService {
    private final GalaxyRepository galaxyRepository;
    private final UserRepository userRepository;

    // 그룹 생성
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

    // 그룹 참여
    public GalaxyResponse joinGalaxyByCode(String code) {
        Galaxy galaxy = galaxyRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("해당 코드를 가진 Galaxy를 찾을 수 없습니다."));
        return GalaxyResponse.from(galaxy, "그룹에 성공적으로 참여하였습니다.");
    }

    // 그룹 프로필 조회
    public GalaxyProfileResponse getGalaxyProfile(Long galaxyId) {
        Galaxy galaxy = findGalaxyById(galaxyId);
        return GalaxyProfileResponse.from(galaxy, userRepository.countByGalaxy(galaxy));
    }

    // 그룹명 수정
    public GalaxyResponse updateGalaxyName(Long galaxyId, UpdateGalaxyNameRequest request) {
        Galaxy galaxy = findGalaxyById(galaxyId);
        galaxy.updateName(request.getName());
        galaxyRepository.save(galaxy);
        return GalaxyResponse.from(galaxy, "그룹명이 성공적으로 수정되었습니다.");
    }

    // 그룹 옵션 수정
    public GalaxyResponse updateGalaxyOptions(Long galaxyId, UpdateGalaxyOptionsRequest request) {
        Galaxy galaxy = findGalaxyById(galaxyId);
        galaxy.updateOptions(request.getGender(), request.getAge(), request.getRelationship());
        galaxyRepository.save(galaxy);
        return GalaxyResponse.from(galaxy, "그룹 옵션이 성공적으로 수정되었습니다.");
    }

    // 그룹 코드 조회
    public String getGalaxyCode(Long galaxyId) {
        Galaxy galaxy = findGalaxyById(galaxyId);
        return galaxy.getCode();
    }

    // 그룹 삭제
    public GalaxyResponse deleteGalaxy(Long galaxyId) {
        Galaxy galaxy = findGalaxyById(galaxyId);
        galaxyRepository.delete(galaxy);
        return GalaxyResponse.from(galaxy, "그룹이 성공적으로 삭제되었습니다.");
    }

    // 캐시 조회
    public CashResponse getGalaxyCash(Long galaxyId) {
        Galaxy galaxy = findGalaxyById(galaxyId);
        return CashResponse.from(galaxy);
    }

    private String generateGroupCode() {
        String code = UUID.randomUUID().toString().substring(0, 8);
        if (galaxyRepository.findByCode(code).isPresent()) {
            return generateGroupCode();
        }
        return code;
    }

    public Galaxy findGalaxyById(Long galaxyId) {
        return galaxyRepository.findById(galaxyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 Galaxy를 찾을 수 없습니다."));
    }
}