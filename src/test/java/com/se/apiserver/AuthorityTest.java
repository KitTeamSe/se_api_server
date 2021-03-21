package com.se.apiserver;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.entity.account.AccountType;
import com.se.apiserver.domain.entity.account.InformationOpenAgree;
import com.se.apiserver.domain.entity.authority.Authority;
import com.se.apiserver.domain.entity.authority.AuthorityGroup;
import com.se.apiserver.domain.entity.authority.AuthorityGroupAccountMapping;
import com.se.apiserver.domain.entity.authority.AuthorityGroupAuthorityMapping;
import com.se.apiserver.repository.account.AccountJpaRepository;
import com.se.apiserver.repository.authority.AuthorityGroupAccountMappingJpaRepository;
import com.se.apiserver.repository.authority.AuthorityGroupAuthorityMappingJpaRepository;
import com.se.apiserver.repository.authority.AuthorityGroupJpaRepository;
import com.se.apiserver.repository.authority.AuthorityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
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
