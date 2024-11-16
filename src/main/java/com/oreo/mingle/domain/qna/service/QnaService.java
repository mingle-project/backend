package com.oreo.mingle.domain.qna.service;

import com.oreo.mingle.domain.qna.repository.AnswerRepository;
import com.oreo.mingle.domain.qna.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
}
