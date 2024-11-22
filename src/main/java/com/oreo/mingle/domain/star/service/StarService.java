package com.oreo.mingle.domain.star.service;

import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.domain.galaxy.service.GalaxyService;
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

    private final GalaxyService galaxyService;

    //메인별 선택하기
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
                mainstar.getStar().getColor(),
                mainstar.getStar().getImage(),
                mainstar.getStar().getRarity(),
                mainstar.isMain()
        );
    }
    //메인 별 조회하기
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
                collectionStar.getStar().getColor(),
                collectionStar.getStar().getImage(),
                collectionStar.getStar().getRarity(),
                collectionStar.isMain()
        );
    }

    //별 목록 가지고 오기(도감)
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
                        star.getStar().getName(),
                        star.getStar().getColor(),
                        star.getStar().getImage(),
                        star.getStar().getRarity(), // 필요한 경우 추가 필드
                        star.isMain()
                ))
                .collect(Collectors.toList());
    }

    //육성별 조회하기
    @Transactional(readOnly = true)
    public PetStarResponse getPetStar(Long galaxyId) {
        Galaxy galaxy = galaxyService.findGalaxyById(galaxyId);
        PetStar petStar = petStarRepository.findByGalaxy(galaxy)
                .orElseThrow(() -> new IllegalArgumentException("해당 우주에는 해당하는 육성별이 존재하지 않습니다."));

        return new PetStarResponse(
                petStar.getId(),
                petStar.getGalaxy().getId(),
                petStar.getStar().getId(),
                petStar.getStar().getName(),
                petStar.getStar().getColor(),
                petStar.getStar().getImage(),
                petStar.getStar().getRarity(),
                petStar.getLevel(),
                petStar.getPoint()
        );
    }

    //새로운 별 육성하기
    @Transactional
    public PetStarResponse createNewPetStar(Long galaxyId) {
        Galaxy galaxy = galaxyService.findGalaxyById(galaxyId);

        // Galaxy에 해당하는 육성별(PetStar) 찾기
        PetStar petStar = petStarRepository.findByGalaxy(galaxy).orElse(null);

        if (petStar == null) {
            // 육성별이 없을 경우: 새로운 육성별 생성
            Star randomStar = starRepository.findRandomStar()
                    .orElseThrow(() -> new IllegalStateException("랜덤 별을 가져올 수 없습니다."));

            petStar = PetStar.builder()
                    .galaxy(galaxy)
                    .star(randomStar)
                    .level(1) // 초기 레벨
                    .point(0) // 초기 포인트
                    .build();

            PetStar newPetStar = petStarRepository.save(petStar);

            return new PetStarResponse(
                    newPetStar.getId(),
                    newPetStar.getGalaxy().getId(),
                    newPetStar.getStar().getId(),
                    newPetStar.getStar().getName(),
                    newPetStar.getStar().getColor(),
                    newPetStar.getStar().getImage(),
                    newPetStar.getStar().getRarity(),
                    newPetStar.getLevel(),
                    newPetStar.getPoint()
            );
        } else {
            // 육성별이 이미 존재할 경우: 기존 육성별을 CollectionStar로 이동
            CollectionStar collectionStar = CollectionStar.builder()
                    .galaxy(petStar.getGalaxy())
                    .star(petStar.getStar())
                    .isMain(false)
                    .build();
            collectionStarRepository.save(collectionStar);

            // 새로운 랜덤 별 가져오기
            Star randomStar = starRepository.findRandomStar()
                    .orElseThrow(() -> new IllegalStateException("랜덤 별을 가져올 수 없습니다."));

            // PetStar의 상태 변경
            petStar.changeStar(randomStar);
            petStar.resetLevelAndPoints();
            PetStar updatedPetStar = petStarRepository.save(petStar);

            return new PetStarResponse(
                    updatedPetStar.getId(),
                    updatedPetStar.getGalaxy().getId(),
                    updatedPetStar.getStar().getId(),
                    updatedPetStar.getStar().getName(),
                    updatedPetStar.getStar().getColor(),
                    updatedPetStar.getStar().getImage(),
                    updatedPetStar.getStar().getRarity(),
                    updatedPetStar.getLevel(),
                    updatedPetStar.getPoint()
            );
        }
    }

    //모두 답변하면 포인트 1 증가
    public void savingPoint(Long galaxyId){
        Galaxy galaxy = galaxyService.findGalaxyById(galaxyId);
        // PetStar 조회
        Optional<PetStar> petStarOptional = petStarRepository.findByGalaxy(galaxy);

        // PetStar가 존재하지 않으면 예외를 던짐
        PetStar petStar = petStarOptional.orElseThrow(() ->
                new IllegalArgumentException("해당 우주에 육성 별 조회를 실패했습니다.")
        );

        // 포인트 증가 로직
        int currentPoint = petStar.getPoint();
        petStar.changePoint(currentPoint + 1);

        // 변경된 PetStar를 저장
        petStarRepository.save(petStar);
    }
}
