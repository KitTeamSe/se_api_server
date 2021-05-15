package com.se.apiserver.v1.authority.infra.repository;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthorityGroupAccountMappingJpaRepository extends JpaRepository<AuthorityGroupAccountMapping, Long> {

    @Query("select agam " +
            "from AuthorityGroupAccountMapping agam " +
            "where agam.account.accountId = :accountId " +
            "and agam.authorityGroup.authorityGroupId = :authorityGroupId")
    Optional<AuthorityGroupAccountMapping> findByAccountIdAndAuthorityGroupId(Long accountId, Long authorityGroupId);

}
