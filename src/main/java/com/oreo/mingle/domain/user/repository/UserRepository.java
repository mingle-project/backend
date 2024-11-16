package com.oreo.mingle.domain.user.repository;

import com.oreo.mingle.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
