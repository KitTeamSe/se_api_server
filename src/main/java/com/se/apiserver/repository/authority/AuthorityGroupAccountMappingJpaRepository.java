package com.se.apiserver.repository.authority;

import com.se.apiserver.domain.entity.authority.AuthorityGroup;
import com.se.apiserver.domain.entity.authority.AuthorityGroupAccountMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityGroupAccountMappingJpaRepository extends JpaRepository<AuthorityGroupAccountMapping, Long> {

}
