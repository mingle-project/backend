package com.oreo.mingle.domain.galaxy.service;

import com.oreo.mingle.domain.galaxy.dto.*;
import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.domain.galaxy.repository.GalaxyRepository;
import com.oreo.mingle.domain.qna.service.QnaService;
import com.oreo.mingle.domain.user.dto.UserResponse;
import com.oreo.mingle.domain.user.entity.User;
import com.oreo.mingle.domain.user.repository.UserRepository;
import com.oreo.mingle.domain.user.service.UserService;
import com.oreo.mingle.global.service.GlobalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GalaxyService {
    private final GalaxyRepository galaxyRepository;
    private final UserRepository userRepository;

    private final GlobalService globalService;

    // 그룹 생성
    public GalaxyResponse createGalaxy(Long userId, CreateGalaxyRequest request) {
        String code = generateGroupCode();
        Galaxy newGalaxy = Galaxy.builder()
                .name(request.getName())
                .code(code)
                .gender(request.getGender())
                .age(request.getAge())
                .relationship(request.getRelationship())
                .build();
        galaxyRepository.save(newGalaxy);
        globalService.getOrCreateQuestion(newGalaxy);
        User user = globalService.findUserByUserId(userId);
        user.joinGalaxy(newGalaxy);
        userRepository.save(user);

        return GalaxyResponse.from(newGalaxy, "그룹이 성공적으로 생성되었습니다.");
    }

    // 그룹 참여
    public GalaxyResponse joinGalaxyByCode(Long userId, String code) {
        Galaxy galaxy = galaxyRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("해당 코드를 가진 Galaxy를 찾을 수 없습니다."));
        User user = globalService.findUserByUserId(userId);
        user.joinGalaxy(galaxy);
        userRepository.save(user);
        return GalaxyResponse.from(galaxy, "그룹에 성공적으로 참여하였습니다.");
    }

    // 질문 시작하기
    public GalaxyResponse updateQuestionStarted(Long userId) {
        Galaxy galaxy = globalService.findGalaxyByUserId(userId);
        galaxy.startQuestion();
        return GalaxyResponse.from(galaxy, "질문이 성공적으로 시작되었습니다.");
    }

    // 그룹 프로필 조회
    public GalaxyProfileResponse getMyGalaxyProfile(Long userId) {
        User user = globalService.findUserByUserId(userId);
        Galaxy galaxy = user.getGalaxy();
        int usersCount = userRepository.countByGalaxy(galaxy);
        List<UserResponse> users = userRepository.findByGalaxy(galaxy).stream()
                .map(member -> UserResponse.from(member, null))
                .toList();
        return GalaxyProfileResponse.from(galaxy, usersCount, users);
    }

    // 그룹명 수정
    public GalaxyResponse updateGalaxyName(Long galaxyId, UpdateGalaxyNameRequest request) {
        Galaxy galaxy = globalService.findGalaxyById(galaxyId);
        galaxy.updateName(request.getName());
        galaxyRepository.save(galaxy);
        return GalaxyResponse.from(galaxy, "그룹명이 성공적으로 수정되었습니다.");
    }

    // 그룹 옵션 수정
    public GalaxyResponse updateGalaxyOptions(Long galaxyId, UpdateGalaxyOptionsRequest request) {
        Galaxy galaxy = globalService.findGalaxyById(galaxyId);
        galaxy.updateOptions(request.getGender(), request.getAge(), request.getRelationship());
        galaxyRepository.save(galaxy);
        return GalaxyResponse.from(galaxy, "그룹 옵션이 성공적으로 수정되었습니다.");
    }

    // 그룹 코드 조회
    public String getGalaxyCode(Long galaxyId) {
        Galaxy galaxy = globalService.findGalaxyById(galaxyId);
        return galaxy.getCode();
    }

    // 그룹 삭제
    public GalaxyResponse deleteGalaxy(Long galaxyId) {
        Galaxy galaxy = globalService.findGalaxyById(galaxyId);
        galaxyRepository.delete(galaxy);
        return GalaxyResponse.from(galaxy, "그룹이 성공적으로 삭제되었습니다.");
    }

    // 캐시 조회
    public CashResponse getGalaxyCash(Long galaxyId) {
        Galaxy galaxy = globalService.findGalaxyById(galaxyId);
        return CashResponse.from(galaxy);
    }

    private String generateGroupCode() {
        String code = UUID.randomUUID().toString().substring(0, 8);
        if (galaxyRepository.findByCode(code).isPresent()) {
            return generateGroupCode();
        }
        return code;
    }
}
