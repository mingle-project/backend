package com.oreo.mingle.domain.galaxy.controller;

import com.oreo.mingle.domain.galaxy.dto.CreateGalaxyRequest;
import com.oreo.mingle.domain.galaxy.dto.GalaxyResponse;
import com.oreo.mingle.domain.galaxy.dto.JoinGalaxyRequest;
import com.oreo.mingle.domain.galaxy.service.GalaxyService;
import com.oreo.mingle.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}