package com.se.apiserver.v1.authority.infra.repository;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityGroupJpaRepository extends JpaRepository<AuthorityGroup, Long> {

}
