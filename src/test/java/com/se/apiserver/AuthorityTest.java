package com.se.apiserver;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAccountMappingJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAuthorityMappingJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AuthorityTest {

  @Autowired
  private AuthorityJpaRepository authorityJpaRepository;
  @Autowired
  private AccountJpaRepository accountJpaRepository;
  @Autowired
  private AuthorityGroupJpaRepository authorityGroupJpaRepository;
  @Autowired
  private AuthorityGroupAccountMappingJpaRepository authorityGroupAccountMappingJpaRepository;
  @Autowired
  private AuthorityGroupAuthorityMappingJpaRepository authorityGroupAuthorityMappingJpaRepository;

  @org.junit.jupiter.api.Test
  void test() {
    Account account = Account.builder()
        .idString("string")
        .password("dasdasd")
        .email("djh20@naver.com")
        .informationOpenAgree(InformationOpenAgree.AGREE)
        .name("junhodo")
        .studentId("20150437")
        .phoneNumber("01030045950")
        .type(AccountType.STUDENT)
        .nickname("junhodo")
        .build();
    accountJpaRepository.save(account);

    Account account2 = Account.builder()
        .idString("string2")
        .password("dasdasd")
        .email("djh20@naver.com")
        .informationOpenAgree(InformationOpenAgree.AGREE)
        .name("junhodo")
        .studentId("20150437")
        .phoneNumber("01030045950")
        .type(AccountType.STUDENT)
        .nickname("junhodo")
        .build();
    accountJpaRepository.save(account2);

    //

    AuthorityGroup authorityGroup = AuthorityGroup.builder()
        .description("test")
        .name("member")
        .build();
    authorityGroupJpaRepository.save(authorityGroup);

    AuthorityGroup authorityGroup2 = AuthorityGroup.builder()
        .description("test")
        .name("admin")
        .build();
    authorityGroupJpaRepository.save(authorityGroup2);

    //

    Authority authority = Authority.builder()
        .nameEng("readPost")
        .nameKor("게시판 읽기")
        .build();
    authorityJpaRepository.save(authority);

    Authority authority2 = Authority.builder()
        .nameEng("createPost")
        .nameKor("게시판 쓰기")
        .build();
    authorityJpaRepository.save(authority2);

    //

    AuthorityGroupAuthorityMapping authorityGroupAuthorityMapping = AuthorityGroupAuthorityMapping
        .builder()
        .authorityGroup(authorityGroup)
        .authority(authority)
        .build();
    authorityGroupAuthorityMappingJpaRepository.save(authorityGroupAuthorityMapping);

    AuthorityGroupAuthorityMapping authorityGroupAuthorityMapping2 = AuthorityGroupAuthorityMapping
        .builder()
        .authorityGroup(authorityGroup)
        .authority(authority2)
        .build();
    authorityGroupAuthorityMappingJpaRepository.save(authorityGroupAuthorityMapping2);

    //

    AuthorityGroupAccountMapping authorityGroupAccountMapping = AuthorityGroupAccountMapping.builder()
        .authorityGroup(authorityGroup)
        .account(account)
        .build();
    authorityGroupAccountMappingJpaRepository.save(authorityGroupAccountMapping);

    AuthorityGroupAccountMapping authorityGroupAccountMapping2 = AuthorityGroupAccountMapping.builder()
        .authorityGroup(authorityGroup2)
        .account(account2)
        .build();
    authorityGroupAccountMappingJpaRepository.save(authorityGroupAccountMapping2);

    //

    List<Authority> authorityList = authorityJpaRepository.findByAccountId(account.getAccountId());
  }
}
