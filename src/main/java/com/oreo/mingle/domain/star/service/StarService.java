package com.oreo.mingle.domain.star.service;

import com.oreo.mingle.domain.star.dto.response.CollectionStarResponse;
import com.oreo.mingle.domain.star.dto.response.PetStarResponse;
import com.oreo.mingle.domain.star.entity.CollectionStar;
import com.oreo.mingle.domain.star.entity.PetStar;
import com.oreo.mingle.domain.star.entity.Star;
import com.oreo.mingle.domain.star.repository.CollectionStarRepository;
import com.oreo.mingle.domain.star.repository.PetStarRepository;
import com.oreo.mingle.domain.star.repository.StarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    public CollectionStarResponse updateMainStar(Long galaxyId, Long starId) {
        // 기존의 메인별이 있으면 is_main을 false로 변경
        collectionStarRepository.findByIsMainTrueAndGalaxyId(galaxyId).ifPresent(existingMainStar -> {
            existingMainStar.unsetAsMain();
            collectionStarRepository.save(existingMainStar);
        });

        // 새로운 main으로 설정할 별 찾기
        CollectionStar collectionStar = collectionStarRepository.findByGalaxyIdAndStarId(galaxyId, starId)
                .orElseThrow(() -> new IllegalArgumentException("해당 galaxyId와 starId에 해당하는 CollectionStar가 없습니다."));

        // 새로운 main 설정
        collectionStar.setAsMain();
        CollectionStar mainstar = collectionStarRepository.save(collectionStar);
        return new CollectionStarResponse(
                mainstar.getId(),
                mainstar.getGalaxy().getId(),
                mainstar.getStar().getId(),
                mainstar.getStar().getName(),
                mainstar.isMain()
        );
    }

    public List<CollectionStarResponse> getStars(Long galaxyId) {
        // CollectionStar 엔티티를 조회
        List<CollectionStar> collectionStars = collectionStarRepository.findByGalaxyId(galaxyId);

        // 예외 처리: 조회된 데이터가 없을 경우
        if (collectionStars.isEmpty()) {
            throw new IllegalArgumentException("해당 우주에서는 별을 찾을 수 없습니다.");
        }

        // 변환 로직
        return collectionStars.stream()
                .map(star -> new CollectionStarResponse(
                        star.getId(),
                        star.getGalaxy().getId(),
                        star.getStar().getId(),
                        star.getStar().getName(), // 필요한 경우 추가 필드
                        star.isMain()
                ))
                .collect(Collectors.toList());
    }

    public CollectionStarResponse getMainStar(Long galaxyId) {
        Optional<CollectionStar> optionalCollectionStar = collectionStarRepository.findByIsMainTrueAndGalaxyId(galaxyId);

        if (optionalCollectionStar.isEmpty()) {
            throw new IllegalArgumentException("해당 우주에는 메인 별이 없습니다.");
        }

        CollectionStar collectionStar = optionalCollectionStar.get();
        return new CollectionStarResponse(
                collectionStar.getId(),
                collectionStar.getGalaxy().getId(),
                collectionStar.getStar().getId(),
                collectionStar.getStar().getName(),
                collectionStar.isMain()
        );
    }


    @Transactional(readOnly = true)
    public PetStarResponse getPetStar(Long galaxyId) {
        PetStar petStar = petStarRepository.findByGalaxyId(galaxyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 우주에는 해당하는 육성별이 존재하지 않습니다."));

        return new PetStarResponse(
                petStar.getId(),
                petStar.getGalaxy().getId(),
                petStar.getStar().getId(),
                petStar.getLevel(),
                petStar.getPoint()
        );
    }

    @Transactional
    public PetStarResponse createNewPetStar(Long galaxyId) {
        PetStar petStar = petStarRepository.findByGalaxyId(galaxyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 우주에 육성별은 존재하지 않습니다"));

        CollectionStar collectionStar = CollectionStar.builder()
                .galaxy(petStar.getGalaxy())
                .star(petStar.getStar())
                .isMain(false)
                .build();
        collectionStarRepository.save(collectionStar);

        Star randomStar = starRepository.findRandomStar()
                .orElseThrow(() -> new IllegalStateException("랜덤 별을 가져올 수 없습니다."));

        // PetStar의 상태 변경
        petStar.changeStar(randomStar);
        petStar.resetLevelAndPoints();
        PetStar newPetStar = petStarRepository.save(petStar);

        return new PetStarResponse(
                newPetStar.getId(),
                newPetStar.getGalaxy().getId(),
                newPetStar.getStar().getId(),
                newPetStar.getLevel(),
                newPetStar.getPoint()
        );
    }
}
