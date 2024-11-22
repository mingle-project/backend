package com.oreo.mingle.domain.qna.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.domain.galaxy.entity.enums.Age;
import com.oreo.mingle.domain.galaxy.entity.enums.Gender;
import com.oreo.mingle.domain.galaxy.entity.enums.Relationship;
import com.oreo.mingle.domain.galaxy.service.GalaxyService;
import com.oreo.mingle.domain.qna.dto.*;
import com.oreo.mingle.domain.qna.entity.Answer;
import com.oreo.mingle.domain.qna.entity.Question;
import com.oreo.mingle.domain.qna.entity.enums.QuestionType;
import com.oreo.mingle.domain.qna.repository.AnswerRepository;
import com.oreo.mingle.domain.qna.repository.QuestionRepository;
import com.oreo.mingle.domain.star.service.StarService;
import com.oreo.mingle.domain.user.entity.User;
import com.oreo.mingle.domain.user.repository.UserRepository;
import com.oreo.mingle.domain.user.service.UserService;
import com.oreo.mingle.global.service.GlobalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    private final GlobalService globalService;

    // 1. groupId와 date를 사용해 당일 질문 조회
    @Transactional(readOnly = true)
    public QuestionResponse findTodayQuestion(Long userId) {
        Galaxy galaxy = globalService.findGalaxyByUserId(userId);
        if (galaxy.getIsStarted()) {
            throw new IllegalStateException("아직 질문이 시작되지 않았습니다.");
        }
        Question question = globalService.getOrCreateQuestion(galaxy);
        return QuestionResponse.from(question);
    }

    // 2. 질문 답변 작성
    @Transactional
    public AnswerResponse submitAnswer(Long userId, Long questionId, String content) {
        User user = globalService.findUserByUserId(userId);
        Question question = findQuestionByQuestionId(questionId);
        Answer newAnswer = Answer.builder()
                .user(user)
                .question(question)
                .content(content)
                .build();
        answerRepository.save(newAnswer);
        if (checkAllAnswered(newAnswer.getQuestion())) {
            globalService.savingPoint(question.getGalaxy().getId());
        }
        return AnswerResponse.from(newAnswer);
    }

    //3 모든 답변이 완료됐는지 확인
    @Transactional(readOnly = true)
    private boolean checkAllAnswered(Question question) {
        Galaxy galaxy = question.getGalaxy();
        int usersCount = userRepository.countByGalaxy(galaxy);
        int answerCount = answerRepository.countByQuestion(question);
        return usersCount == answerCount;
    }

    // 4. 현재까지 받은 질문 목록 조회
    @Transactional(readOnly = true)
    public QuestionListResponse getReceivedQuestionsByUser(Long userId) {
        Galaxy galaxy = globalService.findGalaxyByUserId(userId);
        List<Question> questions = questionRepository.findByGalaxy(galaxy);
        if (questions.isEmpty()) {
            throw new IllegalArgumentException("은하가 받은 질문이 없습니다.");
        }
        List<QuestionResponse> questionResponses = questions.stream()
                .map(QuestionResponse::from)
                .collect(Collectors.toList());
        return QuestionListResponse.builder()
                .questions(questionResponses)
                .build();
    }

    // 5. 현재까지 답한 질문들의 답변 조회
    @Transactional(readOnly = true)
    public AnswerListResponse getAnswerListByUser(Long userId, Long questionId) {
        User user = globalService.findUserByUserId(userId);
        Question question = findQuestionByQuestionId(questionId);
        if (!checkAllAnswered(question)) {
            throw new IllegalStateException("아직 모든 답변이 작성되지 않았습니다.");
        }

        Answer myAnswer = answerRepository.findByUserAndQuestion(user, question)
                .orElseThrow(() -> new EntityNotFoundException("나의 답변이 없습니다."));
        if (myAnswer.getContent().isBlank()) {
            throw new IllegalStateException("답변을 작성하지 않아 친구들의 답변을 확인할 수 없습니다.");
        }

        List<Answer> answers = answerRepository.findByQuestion(question);
        List<AnswerResponse> answerResponses = answers.stream()
                .map(AnswerResponse::from) // Answer -> AnswerResponse 매핑
                .collect(Collectors.toList());
        return AnswerListResponse.builder()
                .questionId(question.getId())
                .galaxyId(question.getId())
                .subject(question.getSubject())
                .date(question.getDate())
                .answers(answerResponses)
                .build();
    }

    @Transactional(readOnly = true)
    private Question findQuestionByQuestionId(Long question) {
        return questionRepository.findById(question)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 Question을 찾을 수 없습니다."));
    }
}