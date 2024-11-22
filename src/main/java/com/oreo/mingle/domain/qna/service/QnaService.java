package com.oreo.mingle.domain.qna.service;

import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.domain.qna.dto.*;
import com.oreo.mingle.domain.qna.entity.Answer;
import com.oreo.mingle.domain.qna.entity.Question;
import com.oreo.mingle.domain.qna.entity.enums.QuestionType;
import com.oreo.mingle.domain.qna.repository.AnswerRepository;
import com.oreo.mingle.domain.qna.repository.QuestionRepository;
import com.oreo.mingle.domain.star.service.StarService;
import com.oreo.mingle.domain.user.entity.User;
import com.oreo.mingle.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    
    private final StarService starService;

    // 1. groupId와 date를 사용해 당일 질문 조회
    public Question getQuestionByGroupAndDate(Long questionId) {
        LocalDate today = LocalDate.now();
        Question question = findQuestionByQuestionId(questionId);
        return questionRepository.findByGalaxyAndDate(question.getGalaxy(), today)
                .orElseThrow(() -> new IllegalArgumentException("해당 날짜에 질문이 존재하지 않습니다."));
    }

    // 2. 질문 답변 작성
    public AnswerResponse submitAnswer(Long userId, Long questionId, String content) {
        // 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. 질문 조회
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 질문입니다."));

        // 예외처리 1) 공백값
        if (content.isBlank()) {
            throw new IllegalArgumentException("답변을 포기했습니다.");
        }

        Answer newAnswer = Answer.builder()
                .user(user)
                .question(question)
                .content(content)
                .build();
        answerRepository.save(newAnswer);

        if (checkAllAnswered(newAnswer.getQuestion())) {
            starService.savingPoint(question.getGalaxy().getId());
        }

        //return 값
        return AnswerResponse.from(newAnswer);
    }

    //3 모든 답변이 완료됐는지 확인
    public boolean checkAllAnswered(Question question) {
        Galaxy galaxy = question.getGalaxy();
        int usersCount = userRepository.countByGalaxy(galaxy);
        int answerCount = answerRepository.countByQuestion(question);
        return usersCount == answerCount;
    }
    // 4. 현재까지 받은 질문 목록 조회
    public QuestionListResponse getReceivedQuestionsByUser(Long galaxyId) {
        List<Question> questions = questionRepository.findByGalaxyId(galaxyId);

        // 질문이 존재하지 않을 경우 예외 처리
        if (questions.isEmpty()) {
            throw new IllegalArgumentException("은하가 받은 질문이 없습니다.");
        }
        // Question -> QuestionResponse 변환
        List<QuestionResponse> questionResponses = questions.stream()
                .map(QuestionResponse::from) // QuestionResponse의 from 메서드 활용
                .collect(Collectors.toList());

        return QuestionListResponse.builder()
                .questions(questionResponses) // 변환된 DTO 리스트를 사용
                .build();

    }

    // 5. 현재까지 답한 질문들의 답변 조회
    public AnswerListResponse getAnswerListByUser(Long questionId) {
        // 질문 정보 조회
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 답변입니다. "));

        //질문에 대한 답변 리스트 조회
        List<Answer> answers = answerRepository.findByQuestion(question);

        // 질문 답변이 없을 때
        if (answers.isEmpty()) {
            throw new IllegalArgumentException("답변을 하지 않았습니다.");
        }

        // Answer Entity -> AnswerResponse 변환
        List<AnswerResponse> answerResponses = answers.stream()
                .map(AnswerResponse::from) // Answer -> AnswerResponse 매핑
                .collect(Collectors.toList());

        return AnswerListResponse.builder()
                .questionId(question.getId())
                .galaxyId(question.getId())
                .subject(question.getSubject())
                .date(question.getDate())
                .answer(answerResponses)
                .build();
    }

    private Question findQuestionByQuestionId(Long question) {
        return questionRepository.findById(question)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 Question을 찾을 수 없습니다."));
    }
}


