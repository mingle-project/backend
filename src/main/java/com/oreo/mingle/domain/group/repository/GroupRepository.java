package com.oreo.mingle.domain.group.repository;

import com.oreo.mingle.domain.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
