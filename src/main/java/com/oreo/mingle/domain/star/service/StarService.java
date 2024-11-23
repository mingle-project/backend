package com.oreo.mingle.domain.star.service;

import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.domain.star.dto.response.CollectionStarResponse;
import com.oreo.mingle.domain.star.dto.response.PetStarResponse;
import com.oreo.mingle.domain.star.entity.CollectionStar;
import com.oreo.mingle.domain.star.entity.PetStar;
import com.oreo.mingle.domain.star.entity.Star;
import com.oreo.mingle.domain.star.entity.enums.Level;
import com.oreo.mingle.domain.star.repository.CollectionStarRepository;
import com.oreo.mingle.domain.star.repository.PetStarRepository;
import com.oreo.mingle.domain.star.repository.StarRepository;
import com.oreo.mingle.global.service.GlobalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StarService {
    private final StarRepository starRepository;
    private final CollectionStarRepository collectionStarRepository;
    private final PetStarRepository petStarRepository;

    private final GlobalService globalService;

    @Value("${mingle.image-url.munzi}")
    private String munziImage;

    @Value("${mingle.image-url.littlestar}")
    private String littlsstarImage;

    //메인별 선택하기
    @Transactional
    public CollectionStarResponse updateMainStar(Long userId, Long collectionStarId) {
        Galaxy galaxy = globalService.findGalaxyByUserId(userId);
        collectionStarRepository.findByIsMainTrueAndGalaxy(galaxy).ifPresent(this::unsetMainStar);
        CollectionStar collectionStar = collectionStarRepository.findById(collectionStarId)
                .orElseThrow(() -> new IllegalArgumentException("해당 collectionStarId에 해당하는 CollectionStar가 없습니다."));
        setMainStar(collectionStar);
        return buildCollectionStarResponse(collectionStar);
    }

    private void unsetMainStar(CollectionStar existingMainStar) {
        existingMainStar.unsetAsMain();
        collectionStarRepository.save(existingMainStar);
    }

    private void setMainStar(CollectionStar collectionStar) {
        collectionStar.setAsMain();
        collectionStarRepository.save(collectionStar);
    }

    // 메인 별 조회하기
    public CollectionStarResponse getMainStar(Long userId) {
        Galaxy galaxy = globalService.findGalaxyByUserId(userId);
        CollectionStar collectionStar = collectionStarRepository.findByIsMainTrueAndGalaxy(galaxy)
                .orElseThrow(() -> new IllegalArgumentException("해당 우주에는 메인 별이 없습니다."));
        return buildCollectionStarResponse(collectionStar);
    }

    // 별 목록 가지고 오기 (도감)
    public List<CollectionStarResponse> getStars(Long userId) {
        Galaxy galaxy = globalService.findGalaxyByUserId(userId);
        List<CollectionStar> collectionStars = collectionStarRepository.findByGalaxy(galaxy);
        if (collectionStars.isEmpty()) {
            throw new IllegalArgumentException("해당 우주에서는 별을 찾을 수 없습니다.");
        }
        return collectionStars.stream()
                .map(this::buildCollectionStarResponse)
                .collect(Collectors.toList());
    }

    private CollectionStarResponse buildCollectionStarResponse(CollectionStar collectionStar) {
        return new CollectionStarResponse(
                collectionStar.getId(),
                collectionStar.getGalaxy().getId(),
                collectionStar.getStar().getId(),
                collectionStar.getStar().getName(),
                findImageUrlByLevelAndStar(Level.ADULT, collectionStar.getStar()),
                collectionStar.getStar().getRarity(),
                collectionStar.isMain()
        );
    }

    //육성별 조회하기
    @Transactional(readOnly = true)
    public PetStarResponse getPetStar(Long userId) {
        Galaxy galaxy = globalService.findGalaxyByUserId(userId);
        PetStar petStar = findPetStarByGalaxy(galaxy);
        String imageUrl = findImageUrlByLevelAndStar(petStar.getLevel(), petStar.getStar());
        return new PetStarResponse(
                petStar.getId(),
                petStar.getGalaxy().getId(),
                petStar.getStar().getId(),
                petStar.getStar().getName(),
                imageUrl,
                petStar.getStar().getRarity(),
                petStar.getLevel(),
                petStar.getPoint()
        );
    }

    //새로운 별 육성하기
    @Transactional
    public PetStarResponse createNewPetStar(Long userId) {
        Galaxy galaxy = globalService.findGalaxyByUserId(userId);
        PetStar petStar = galaxy.getPetStar();

        if (petStar == null) {
            petStar = createNewPetStarEntity(galaxy);
        } else {
            updateExistingPetStar(petStar);
        }
        return buildPetStarResponse(petStar);
    }

    private PetStar createNewPetStarEntity(Galaxy galaxy) {
        Star randomStar = findRandomStar();
        PetStar petStar = PetStar.builder()
                .galaxy(galaxy)
                .star(randomStar)
                .level(Level.MUNZI) // 초기 레벨
                .point(0) // 초기 포인트
                .build();
        return petStarRepository.save(petStar);
    }

    private void updateExistingPetStar(PetStar petStar) {
        Star randomStar = findRandomStar();
        petStar.changeStar(randomStar);
        petStar.resetLevelAndPoints();
        petStarRepository.save(petStar);
    }

    private Star findRandomStar() {
        return starRepository.findRandomStar()
                .orElseThrow(() -> new IllegalStateException("랜덤 별을 가져올 수 없습니다."));
    }

    private PetStarResponse buildPetStarResponse(PetStar petStar) {
        return new PetStarResponse(
                petStar.getId(),
                petStar.getGalaxy().getId(),
                petStar.getStar().getId(),
                petStar.getStar().getName(),
                findImageUrlByLevelAndStar(petStar.getLevel(), petStar.getStar()),
                petStar.getStar().getRarity(),
                petStar.getLevel(),
                petStar.getPoint()
        );
    }

    private String findImageUrlByLevelAndStar(Level level, Star star) {
        return switch (level) {
            case MUNZI -> munziImage;
            case LITTLESTAR -> littlsstarImage;
            case BIGSTAR -> star.getBigstarImage();
            case ADULT -> star.getAdultImage();
        };
    }

    private PetStar findPetStarByGalaxy(Galaxy galaxy) {
        return petStarRepository.findByGalaxy(galaxy)
                .orElseThrow(() -> new IllegalArgumentException("해당 그룹에는 육성별이 존재하지 않습니다."));
    }
}
