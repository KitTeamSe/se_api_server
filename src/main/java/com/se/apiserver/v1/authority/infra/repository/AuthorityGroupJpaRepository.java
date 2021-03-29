package com.se.apiserver.v1.authority.infra.repository;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorityGroupJpaRepository extends JpaRepository<AuthorityGroup, Long> {
    Optional<AuthorityGroup> findByName(String name);
    List<AuthorityGroup> findByType(AuthorityGroupType authorityGroupType);
}
