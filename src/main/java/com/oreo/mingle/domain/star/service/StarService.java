package com.oreo.mingle.domain.star.service;

import com.oreo.mingle.domain.star.repository.CollectionStarRepository;
import com.oreo.mingle.domain.star.repository.PetStarRepository;
import com.oreo.mingle.domain.star.repository.StarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StarService {
    private final StarRepository starRepository;
    private final CollectionStarRepository collectionStarRepository;
    private final PetStarRepository petStarRepository;
}
