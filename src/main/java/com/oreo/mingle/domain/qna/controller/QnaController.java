package com.oreo.mingle.domain.qna.controller;

import com.oreo.mingle.domain.qna.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QnaController {
    private final QnaService qnaService;
}
