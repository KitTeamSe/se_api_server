package com.se.apiserver.v1.authority.infra.repository;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityGroupAuthorityMappingJpaRepository extends
    JpaRepository<AuthorityGroupAuthorityMapping, Long> {

}
