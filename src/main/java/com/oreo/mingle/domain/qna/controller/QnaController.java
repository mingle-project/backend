package com.oreo.mingle.domain.qna.controller;

import com.oreo.mingle.domain.qna.dto.*;
import com.oreo.mingle.domain.qna.entity.Question;
import com.oreo.mingle.domain.qna.entity.enums.QuestionType;
import com.oreo.mingle.domain.qna.service.QnaService;
import com.oreo.mingle.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QnaController {
    private final QnaService qnaService;

    //당일 질문 조회 api
    @GetMapping("/questions/today")
    public ResponseEntity<QuestionResponse> getTodayQuestion(Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("Request to GET today's question");
        try {
            QuestionResponse response = qnaService.findTodayQuestion(userId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error fetching today's question: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 질문 답변 작성
    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<AnswerResponse> submitAnswer(Authentication authentication, @PathVariable("question_id") Long questionId, @RequestBody AnswerRequest request) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("Request to POST answer: questionId={}, userId={}, content={}", questionId, userId, request.getContent());
        try {
            AnswerResponse response = qnaService.submitAnswer(userId, questionId, request.getContent());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Error while submitting answer: questionId={}, userId={}, error={}", questionId, userId, e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 질문 목록 조회
    @GetMapping("/galaxy/questions")
    public ResponseEntity<QuestionListResponse> getQuestionList(Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("request to GET galaxy questionList");
        QuestionListResponse response = qnaService.getReceivedQuestionsByUser(userId);
        return ResponseEntity.ok(response);
    }

    // 질문에 대한 답변 조회
    @GetMapping("/questions/{question_id}/answers")
    public ResponseEntity<AnswerListResponse> getAnswerList(Authentication authentication, @PathVariable("question_id")Long questionId) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        log.info("request to GET galaxy answerList with id : {} ", questionId);
        AnswerListResponse response = qnaService.getAnswerListByUser(userId, questionId);
        return ResponseEntity.ok(response);
    }
}
