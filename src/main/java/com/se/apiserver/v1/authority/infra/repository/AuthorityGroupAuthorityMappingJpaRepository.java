package com.se.apiserver.v1.authority.infra.repository;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthorityGroupAuthorityMappingJpaRepository extends JpaRepository<AuthorityGroupAuthorityMapping, Long> {
    @Query("select agam " +
            "from AuthorityGroupAuthorityMapping agam " +
            "where agam.authority.authorityId = :authorityId and agam.authorityGroup.authorityGroupId = :authorityGroupId")
    Optional<AuthorityGroupAuthorityMapping> findByAuthorityAndAuthorityGroupId(Long authorityId, Long authorityGroupId);

    List<AuthorityGroupAuthorityMapping> findAllByAuthorityGroup(AuthorityGroup authorityGroup);

}
