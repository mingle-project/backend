package com.oreo.mingle.domain.user.service;

import com.oreo.mingle.domain.user.dto.SignupRequest;
import com.oreo.mingle.domain.user.dto.UserResponse;
import com.oreo.mingle.domain.user.entity.User;
import com.oreo.mingle.domain.user.entity.enums.Role;
import com.oreo.mingle.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserResponse joinProcess(SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
        User newUser = User.builder()
                .username(signupRequest.getUsername())
                .password(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                .nickname(signupRequest.getNickname())
                .role(Role.USER)
                .build();
        userRepository.save(newUser);

        return UserResponse.from(newUser, "회원가입 성공!");
    }
}
