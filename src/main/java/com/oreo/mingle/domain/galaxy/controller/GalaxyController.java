package com.oreo.mingle.domain.galaxy.controller;

import com.oreo.mingle.domain.galaxy.service.GalaxyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GalaxyController {
    private final GalaxyService GalaxyService;
}
