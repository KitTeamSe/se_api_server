package com.se.apiserver.repository.authority;

import com.se.apiserver.domain.entity.authority.AuthorityGroup;
import com.se.apiserver.domain.entity.authority.AuthorityGroupAuthorityMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityGroupAuthorityMappingJpaRepository extends
    JpaRepository<AuthorityGroupAuthorityMapping, Long> {

}
