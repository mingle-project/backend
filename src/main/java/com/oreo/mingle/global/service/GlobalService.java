package com.oreo.mingle.global.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.domain.galaxy.entity.enums.Age;
import com.oreo.mingle.domain.galaxy.entity.enums.Gender;
import com.oreo.mingle.domain.galaxy.entity.enums.Relationship;
import com.oreo.mingle.domain.galaxy.repository.GalaxyRepository;
import com.oreo.mingle.domain.qna.entity.Question;
import com.oreo.mingle.domain.qna.entity.enums.QuestionType;
import com.oreo.mingle.domain.qna.repository.QuestionRepository;
import com.oreo.mingle.domain.star.entity.PetStar;
import com.oreo.mingle.domain.star.repository.PetStarRepository;
import com.oreo.mingle.domain.user.entity.User;
import com.oreo.mingle.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GlobalService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final GalaxyRepository galaxyRepository;
    private final PetStarRepository petStarRepository;

    @Autowired
    private ObjectMapper objectMapper;
    private Map<String, List<String>> questionsMap;

    @PostConstruct
    public void initQuestionsMap() {
        try (InputStream inputStream = getClass().getResourceAsStream("/static/qnas.json")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("qnas.json 파일을 찾을 수 없습니다.");
            }
            this.questionsMap = objectMapper.readValue(inputStream, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("qnas.json 파일 로드 중 오류가 발생했습니다.", e);
        }
    }

    public User findUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 User를 찾을 수 없습니다."));
    }

    public Galaxy findGalaxyByUserId(Long userId) {
        User user = findUserByUserId(userId);
        return user.getGalaxy();
    }

    //모두 답변하면 포인트 1 증가
    public void savingPoint(Long galaxyId){
        Galaxy galaxy = findGalaxyById(galaxyId);
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

    // 모두 답변하면 10캐시 추가
    public void savingCash(Long galaxyId) {
        // 그룹 조회 및 육성 별 조회
        Galaxy galaxy = findGalaxyById(galaxyId);


        int currentCash = galaxy.getCash();
        galaxy.changeCash(currentCash + 10);
        galaxyRepository.save(galaxy);

    }

    public Question getOrCreateQuestion(Galaxy galaxy) {
        if (this.questionsMap == null) {
            throw new IllegalStateException("questionsMap이 초기화되지 않았습니다.");
        }
        LocalDate today = LocalDate.now();
//        Optional<Question> existingQuestion = questionRepository.findByGalaxyAndDate(galaxy, today);
//        if (existingQuestion.isPresent()) {
//            return existingQuestion.get();
//        }
        QuestionType type = determineQuestionType(galaxy);
        long questionCount = questionRepository.countByGalaxy(galaxy);
        List<String> typeQuestions = questionsMap.get(type.name());
        int questionIndex = (int) (questionCount % typeQuestions.size());
        String selectedQuestion = typeQuestions.get(questionIndex);
        Question newQuestion = Question.builder()
                .galaxy(galaxy)
                .type(type)
                .subject(selectedQuestion)
                .date(today)
                .build();
        return questionRepository.save(newQuestion);
    }

    public QuestionType determineQuestionType(Galaxy galaxy) {
        Gender gender = galaxy.getGender();
        Age age = galaxy.getAge();
        Relationship relationship = galaxy.getRelationship();
        // 동성 그룹
        if (gender == Gender.MALE || gender == Gender.FEMALE) {
            if (age == Age.AGE_10) { // 미성년자
                return switch (relationship) {
                    case ACQUAINTANCE -> QuestionType.A;
                    case CLOSE -> QuestionType.B;
                    case COMFORTABLE -> QuestionType.C;
                };
            } else { // 성인
                return switch (relationship) {
                    case ACQUAINTANCE -> QuestionType.D;
                    case CLOSE -> QuestionType.E;
                    case COMFORTABLE -> QuestionType.F;
                };
            }
        }
        // 혼성 그룹
        else if (gender == Gender.MIXED) {
            if (age == Age.AGE_10) { // 미성년자
                return switch (relationship) {
                    case ACQUAINTANCE -> QuestionType.G;
                    case CLOSE -> QuestionType.H;
                    case COMFORTABLE -> QuestionType.I;
                };
            } else { // 성인
                return switch (relationship) {
                    case ACQUAINTANCE -> QuestionType.J;
                    case CLOSE -> QuestionType.K;
                    case COMFORTABLE -> QuestionType.L;
                };
            }
        }
        throw new IllegalArgumentException("유효하지 않은 그룹 옵션");
    }

    public Galaxy findGalaxyById(Long galaxyId) {
        return galaxyRepository.findById(galaxyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 Galaxy를 찾을 수 없습니다."));
    }
}
