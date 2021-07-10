package com.se.apiserver.v1.authority.infra.repository;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorityJpaRepository extends JpaRepository<Authority, Long> {

  @Query(value =
      "select gau.authority from AuthorityGroupAccountMapping ga, AuthorityGroupAuthorityMapping gau " +
          "where ga.authorityGroup.name = gau.authorityGroup.name and ga.account.accountId = :accountId"
  )
  List<Authority> findByAccountId(Long accountId);

  @Query(value =
      "select gau.authority from AuthorityGroupAuthorityMapping gau where gau.authorityGroup.name = :groupName"
  )
  List<Authority> findByAuthorityGroupName(String groupName);

  Optional<Authority> findByNameEng(String nameEng);

  Optional<Authority> findByNameKor(String nameKor);
}
