package com.se.apiserver.repository.authority;

import com.se.apiserver.domain.entity.authority.AuthorityGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AuthorityGroupJpaRepository extends JpaRepository<AuthorityGroup, Long> {
}
