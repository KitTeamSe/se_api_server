package com.se.apiserver.repository.authority;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.entity.authority.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
