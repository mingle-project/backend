package com.oreo.mingle.domain.user.controller;

import com.oreo.mingle.domain.user.dto.SignupRequest;
import com.oreo.mingle.domain.user.dto.UserResponse;
import com.oreo.mingle.domain.user.service.UserService;
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
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody SignupRequest request) {
        log.info("signup email: {}", request.getUsername());
        UserResponse response = userService.joinProcess(request);
        return ResponseEntity.ok(response);
    }
}