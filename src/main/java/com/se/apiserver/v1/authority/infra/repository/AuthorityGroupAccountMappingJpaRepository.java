package com.se.apiserver.v1.authority.infra.repository;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityGroupAccountMappingJpaRepository extends JpaRepository<AuthorityGroupAccountMapping, Long> {

}
