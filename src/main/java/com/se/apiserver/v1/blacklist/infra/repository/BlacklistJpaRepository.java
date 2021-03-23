package com.se.apiserver.v1.blacklist.infra.repository;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistJpaRepository extends JpaRepository<Blacklist, Long> {
}
