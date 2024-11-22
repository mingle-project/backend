package com.oreo.mingle.domain.qna.controller;

import com.oreo.mingle.domain.qna.dto.*;
import com.oreo.mingle.domain.qna.entity.Question;
import com.oreo.mingle.domain.qna.entity.enums.QuestionType;
import com.oreo.mingle.domain.qna.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QnaController {
    private final QnaService qnaService;

    //당일 질문 조회 api
    @GetMapping("/questions/{question_id}")
    public ResponseEntity<QuestionResponse> getTodayQuestion(@PathVariable("question_id") Long questionId) {
        log.info("Request to GET today's question with ID: {}", questionId);

        try {
            // Service 호출
            Question question = qnaService.getQuestionByGroupAndDate(questionId);

            // Entity -> DTO 변환
            QuestionResponse response = QuestionResponse.from(question);

            // 성공 응답 반환
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error fetching today's question: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    // 질문 답변 작성
    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<AnswerResponse> submitAnswer(
            @PathVariable("questionId") Long questionId,
            @RequestParam("userId") Long userId,
            @RequestBody AnswerRequest request) {
        log.info("Request to POST answer: questionId={}, userId={}, content={}", questionId, userId, request.getContent());

        try {
            // QnaService 호출
            AnswerResponse response = qnaService.submitAnswer(userId, questionId, request.getContent());

            // 성공 로그
            log.info("Answer submitted successfully: questionId={}, userId={}, answerId={}",
                    questionId, userId, response.getAnswerId());

            // HTTP 201 응답 반환
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // 예외 로그
            log.error("Error while submitting answer: questionId={}, userId={}, error={}", questionId, userId, e.getMessage());

            // HTTP 400 응답 반환
            return ResponseEntity.badRequest().body(null);
        }
    }
//    @PostMapping("/questions/{questionId}/answers")
//    public ResponseEntity<AnswerResponse> submitAnswer(@PathVariable("questionId") Long questionId,
//            @RequestParam("userId") Long userId,
//            @RequestBody String content) {
//        log.info("Request to POST galaxy answer : questionId={}, userId={}, content={}", questionId, userId, content);
//        AnswerResponse response = qnaService.submitAnswer(userId, questionId, content);
//        return ResponseEntity.ok(response);
//    }

    // 질문 목록 조회
    @GetMapping("/galaxy/{galaxy_id}/questions")
    public ResponseEntity<QuestionListResponse> getQuestionList(@PathVariable("galaxy_id")Long galaxyId) {
        log.info("request to GET galaxy questionList with id: {}", galaxyId);
        QuestionListResponse response = qnaService.getReceivedQuestionsByUser(galaxyId);
        return ResponseEntity.ok(response);
    }

    // 질문에 대한 답변 조회
    @GetMapping("/questions/{question_id}/answers")
    public ResponseEntity<AnswerListResponse> getAnswerList(@PathVariable("question_id")Long questionId) {
        log.info("request to GET galaxy answerList with id : {} ", questionId);
        AnswerListResponse response = qnaService.getAnswerListByUser(questionId);
        return ResponseEntity.ok(response);
    }
}
