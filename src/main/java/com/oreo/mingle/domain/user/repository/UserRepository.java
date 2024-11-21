package com.oreo.mingle.domain.user.repository;

import com.oreo.mingle.domain.galaxy.entity.Galaxy;
import com.oreo.mingle.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    int countByGalaxy(Galaxy galaxy);
    List<User> findByGalaxy(Galaxy galaxy);
}