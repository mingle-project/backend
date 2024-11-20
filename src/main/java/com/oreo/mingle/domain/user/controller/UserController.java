package com.oreo.mingle.domain.user.controller;

import com.oreo.mingle.domain.user.dto.*;
import com.oreo.mingle.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody SignupRequest request) {
        log.info("signup email: {}", request.getUsername());
        UserResponse response = userService.joinProcess(request);
        return ResponseEntity.ok(response);
    }

    // 닉네임 수정
    @PutMapping("/users/me/nickname")
    public ResponseEntity<UserResponse> putUserNickname(Authentication authentication, UpdateNicknameRequest request) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("request to PUT user nickname with id: {}", userId);
        UserResponse response = userService.updateUserNickname(userId, request.getNickname());
        return ResponseEntity.ok(response);
    }

    // 그룹 탈퇴
    @DeleteMapping("/galaxy/{galaxy_id}/users/me")
    public ResponseEntity<UserResponse> deleteUserFromGroup(Authentication authentication, @PathVariable("galaxy_id") Long galaxyId) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("request to DELETE user from group >> user: {}, galaxy: {}", userId, galaxyId);
        UserResponse response = userService.deleteUserFromGroup(userId, galaxyId);
        return ResponseEntity.ok(response);
    }
}