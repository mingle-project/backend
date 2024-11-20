package com.oreo.mingle.domain.user.service;

import com.oreo.mingle.domain.qna.entity.Question;
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

    public UserResponse updateUserNickname(Long userId, String nickname) {
        User user = findUserByUserId(userId);
        String beforeNickname = user.getNickname();
        user.updateNickname(nickname);
        userRepository.save(user);
        String message = String.format("닉네임을 변경했습니다: %s -> %s", beforeNickname, nickname);
        return UserResponse.from(user, message);
    }

    public UserResponse deleteUserFromGroup(Long userId, Long galaxyId) {
        User user = findUserByUserId(userId);
        userRepository.delete(user);
        return UserResponse.from(user, "사용자가 삭제되었습니다.");
    }

    private User findUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 User를 찾을 수 없습니다."));
    }
}
