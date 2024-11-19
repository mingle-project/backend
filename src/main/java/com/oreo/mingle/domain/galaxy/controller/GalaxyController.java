package com.oreo.mingle.domain.galaxy.controller;

import com.oreo.mingle.domain.galaxy.dto.*;
import com.oreo.mingle.domain.galaxy.entity.enums.Relationship;
import com.oreo.mingle.domain.galaxy.service.GalaxyService;
import com.oreo.mingle.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GalaxyController {
    private final GalaxyService galaxyService;

    // 그룹 생성
    @PostMapping("/galaxy")
    public ResponseEntity<GalaxyResponse> postGalaxy(@RequestBody CreateGalaxyRequest request) {
        log.info("request to POST galaxy: {}", request.getName());
        GalaxyResponse response = galaxyService.createGalaxy(request);
        return ResponseEntity.ok(response);
    }

    // 그룹 참여
    @PostMapping("/galaxy/join")
    public ResponseEntity<GalaxyResponse> postGalaxyJoin(@RequestBody JoinGalaxyRequest request) {
        log.info("request to POST join galaxy by code: {}", request.getGroupCode());
        GalaxyResponse response = galaxyService.joinGalaxyByCode(request.getGroupCode());
        return ResponseEntity.ok(response);
    }

    // 그룹 프로필 조회
    @GetMapping("/galaxy/{galaxy_id}/profile")
    public ResponseEntity<GalaxyProfileResponse> getGalaxyProfile(@PathVariable("galaxy_id") Long galaxyId) {
        log.info("request to GET galaxy profile with id: {}", galaxyId);
        GalaxyProfileResponse response = galaxyService.getGalaxyProfile(galaxyId);
        return ResponseEntity.ok(response);
    }

    // 그룹명 수정
    @PutMapping("/galaxy/{galaxy_id}/name")
    public ResponseEntity<GalaxyResponse> updateGalaxyName(@PathVariable("galaxy_id") Long galaxyId,
                                                           @RequestBody UpdateGalaxyNameRequest request) {
        log.info("request to PUT update galaxy name with id: {}", galaxyId);
        GalaxyResponse response = galaxyService.updateGalaxyName(galaxyId, request);
        return ResponseEntity.ok(response);
    }

    // 그룹 옵션 수정
    @PutMapping("/galaxy/{galaxy_id}/options")
    public ResponseEntity<GalaxyResponse> updateGalaxyOptions(@PathVariable Long galaxyId,
                                                              @RequestBody UpdateGalaxyOptionsRequest request) {
        log.info("request to PUT update galaxy options with id: {}", galaxyId);
        GalaxyResponse response = galaxyService.updateGalaxyOptions(galaxyId, request);
        return ResponseEntity.ok(response);
    }

    // 그룹 코드 조회
    @GetMapping("/galaxy/{galaxy_id}/code")
    public ResponseEntity<String> getGalaxyCode(@PathVariable("galaxy_id") Long galaxyId) {
        log.info("request to GET galaxy code with id: {}", galaxyId);
        String code = galaxyService.getGalaxyCode(galaxyId);
        return ResponseEntity.ok(code);
    }

    // 캐시 조회
    @GetMapping("/galaxy/{galaxy_id}/cash")
    public ResponseEntity<CashResponse> getGalaxyCash(@PathVariable("galaxy_id") Long galaxyId) {
        log.info("request to GET galaxy cash with id: {}", galaxyId);
        CashResponse response = galaxyService.getGalaxyCash(galaxyId);
        return ResponseEntity.ok(response);
    }

    // 그룹 삭제
    @DeleteMapping("/galaxy/{galaxy_id}")
    public ResponseEntity<GalaxyResponse> deleteGalaxy(@PathVariable("galaxy_id") Long galaxyId) {
        log.info("request to DELETE galaxy with id: {}", galaxyId);
        GalaxyResponse response = galaxyService.deleteGalaxy(galaxyId);
        return ResponseEntity.ok(response);
    }
}