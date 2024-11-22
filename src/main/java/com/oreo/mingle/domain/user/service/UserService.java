package com.oreo.mingle.domain.user.service;

import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.domain.qna.entity.Question;
import com.oreo.mingle.domain.user.dto.SignupRequest;
import com.oreo.mingle.domain.user.dto.UserResponse;
import com.oreo.mingle.domain.user.entity.User;
import com.oreo.mingle.domain.user.entity.enums.Role;
import com.oreo.mingle.domain.user.repository.UserRepository;
import com.oreo.mingle.global.service.GlobalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final GlobalService globalService;

    @Transactional
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

    @Transactional
    public UserResponse updateUserNickname(Long userId, String nickname) {
        User user = globalService.findUserByUserId(userId);
        String beforeNickname = user.getNickname();
        user.updateNickname(nickname);
        userRepository.save(user);
        String message = String.format("닉네임을 변경했습니다: %s -> %s", beforeNickname, nickname);
        return UserResponse.from(user, message);
    }

    @Transactional
    public UserResponse deleteUserFromGroup(Long userId) {
        User user = globalService.findUserByUserId(userId);
        user.leaveGalaxy();
        userRepository.save(user);
        return UserResponse.from(user, "그룹에서 탈퇴했습니다.");
    }

    @Transactional(readOnly = true)
    public UserResponse getProfile(Long userId) {
        User user = globalService.findUserByUserId(userId);
        return UserResponse.from(user,"사용자 조회 성공");
    }
}
