package com.oreo.mingle.domain.star.controller;

import com.oreo.mingle.domain.star.dto.request.MainStarChooseRequest;
import com.oreo.mingle.domain.star.dto.response.CollectionStarResponse;
import com.oreo.mingle.domain.star.dto.response.MessageResponse;
import com.oreo.mingle.domain.star.dto.response.PetStarResponse;
import com.oreo.mingle.domain.star.entity.PetStar;
import com.oreo.mingle.domain.star.service.StarService;
import com.oreo.mingle.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StarController {
    private final StarService starService;
  
    @GetMapping("/galaxy/me/stars/pet")
    public ResponseEntity<MessageResponse<PetStarResponse>> getGrowingPetStar(Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("육성 별 조회");
        try {
            PetStarResponse petStarResponse = starService.getPetStar(userId);
            MessageResponse<PetStarResponse> response = new MessageResponse<>("육성별 조회를 성공했습니다.", petStarResponse);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            MessageResponse<PetStarResponse> response = new MessageResponse<>("육성별 조회를 실패했습니다." + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // 새로운 별 육성하기
    @GetMapping("/galaxy/me/stars/pet/new")
    public ResponseEntity<?> createNewStar(Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("새로운 별 육성하기");
        try {
            PetStarResponse petStarResponse = starService.createNewPetStar(userId);
            MessageResponse<PetStarResponse> response = new MessageResponse<>("새로운 육성 별을 생성했습니다!", petStarResponse);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            MessageResponse<PetStarResponse> response = new MessageResponse<>("새로운 육성 별 생성을 실패했습니다." + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @GetMapping("/galaxy/me/stars")
    public ResponseEntity<?> getStars(Authentication authentication){
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("모은 별 조회");
        try {
            List<CollectionStarResponse> collectionStars = starService.getStars(userId);
            MessageResponse<List<CollectionStarResponse>> response = new MessageResponse<>("도감의 모든 별을 불러왔습니다!", collectionStars);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            MessageResponse<List<CollectionStarResponse>> response = new MessageResponse<>("도감의 별을 불러오지 못했습니다." + e.getMessage(),null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // 데이터가 없을 때 404 반환
        }
    }

    // 메인 별 선택
    @PutMapping("/galaxy/me/stars/choice")
    public ResponseEntity<?> updateMainStar(Authentication authentication, @RequestBody MainStarChooseRequest request) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("대표별 선택하기");
        try {
            CollectionStarResponse collectionStarResponse = starService.updateMainStar(userId, request.collectionStarId());
            MessageResponse<CollectionStarResponse> response = new MessageResponse<>("새로운 메인 별을 설정했습니다.", collectionStarResponse);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            MessageResponse<CollectionStarResponse> response = new MessageResponse<>("메인 별 설정을 실패했습니다." + e.getMessage(),null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/galaxy/me/stars/main")
    public ResponseEntity<?> getMainCollectionStar(Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("대표별 조회하기");
        try {
            CollectionStarResponse collectionStarResponse = starService.getMainStar(userId);
            MessageResponse<CollectionStarResponse> response = new MessageResponse<>("메인 별 불러오기 성공했습니다!", collectionStarResponse);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            MessageResponse<CollectionStarResponse> response = new MessageResponse<>("메인 별 불러오기 실패했습니다." + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}

