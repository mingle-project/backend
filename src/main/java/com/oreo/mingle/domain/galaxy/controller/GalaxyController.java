package com.oreo.mingle.domain.galaxy.controller;

import com.oreo.mingle.domain.galaxy.dto.*;
import com.oreo.mingle.domain.galaxy.service.GalaxyService;
import com.oreo.mingle.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GalaxyController {
    private final GalaxyService galaxyService;

    // 그룹 생성
    @PostMapping("/galaxy")
    public ResponseEntity<GalaxyResponse> postGalaxy(Authentication authentication, @RequestBody CreateGalaxyRequest request) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("request to POST galaxy: {}", request.getName());
        GalaxyResponse response = galaxyService.createGalaxy(userId, request);
        return ResponseEntity.ok(response);
    }

    // 그룹 참여
    @PostMapping("/galaxy/join")
    public ResponseEntity<GalaxyResponse> postGalaxyJoin(Authentication authentication, @RequestBody JoinGalaxyRequest request) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("request to POST join galaxy by code: {}", request.getGroupCode());
        GalaxyResponse response = galaxyService.joinGalaxyByCode(userId, request.getGroupCode());
        return ResponseEntity.ok(response);
    }

    // 질문 시작하기
    @PutMapping("/api/galaxy/me/start-question")
    public ResponseEntity<GalaxyResponse> putGalaxyIsStarted(Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("request to GET start question");
        GalaxyResponse response = galaxyService.updateQuestionStarted(userId);
        return ResponseEntity.ok(response);
    }

    // 그룹 프로필 조회
    @GetMapping("/galaxy/me/profile")
    public ResponseEntity<GalaxyProfileResponse> getGalaxyProfile(Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("request to GET my galaxy profile: {}", userId);
        GalaxyProfileResponse response = galaxyService.getMyGalaxyProfile(userId);
        return ResponseEntity.ok(response);
    }

    // 그룹명 수정
    @PutMapping("/galaxy/me/name")
    public ResponseEntity<GalaxyResponse> updateGalaxyName(Authentication authentication,
                                                           @RequestBody UpdateGalaxyNameRequest request) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("request to PUT update galaxy name");
        GalaxyResponse response = galaxyService.updateGalaxyName(userId, request);
        return ResponseEntity.ok(response);
    }

    // 그룹 옵션 수정
    @PutMapping("/galaxy/me/options")
    public ResponseEntity<GalaxyResponse> updateGalaxyOptions(Authentication authentication,
                                                              @RequestBody UpdateGalaxyOptionsRequest request) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("request to PUT update galaxy options");
        GalaxyResponse response = galaxyService.updateGalaxyOptions(userId, request);
        return ResponseEntity.ok(response);
    }

    // 그룹 코드 조회
    @GetMapping("/galaxy/me/code")
    public ResponseEntity<String> getGalaxyCode(Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("request to GET galaxy code");
        String code = galaxyService.getGalaxyCode(userId);
        return ResponseEntity.ok(code);
    }

    // 캐시 조회
    @GetMapping("/galaxy/me/cash")
    public ResponseEntity<CashResponse> getGalaxyCash(Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("request to GET galaxy cash");
        CashResponse response = galaxyService.getGalaxyCash(userId);
        return ResponseEntity.ok(response);
    }

    // 그룹 삭제
    @DeleteMapping("/galaxy/me")
    public ResponseEntity<GalaxyResponse> deleteGalaxy(Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("request to DELETE galaxy");
        GalaxyResponse response = galaxyService.deleteGalaxy(userId);
        return ResponseEntity.ok(response);
    }
}