package com.oreo.mingle.domain.group.service;

import com.oreo.mingle.domain.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
}
