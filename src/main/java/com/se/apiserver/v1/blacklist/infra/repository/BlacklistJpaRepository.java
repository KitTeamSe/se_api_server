package com.se.apiserver.v1.blacklist.infra.repository;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistJpaRepository extends JpaRepository<Blacklist, Long> {
    Optional<Blacklist> findByIp(String ip);
    Optional<Blacklist> findByIdString(String idString);
    List<Blacklist> findAllByReleaseDateBefore(LocalDateTime today);
}
