package com.se.apiserver.v1.authority.domain.usecase.authoritygroupaccountmapping;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupAccountMappingErrorCode;
import com.se.apiserver.v1.authority.infra.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingCreateDto;
import com.se.apiserver.v1.authority.infra.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingReadDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAccountMappingJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthorityGroupAccountMappingCreateUseCaseTest {
    @Autowired
    AuthorityGroupAccountMappingCreateUseCase authorityGroupAccountMappingCreateUseCase;
    @Autowired
    AuthorityGroupAccountMappingJpaRepository authorityGroupAccountMappingJpaRepository;
    @Autowired
    AuthorityGroupJpaRepository authorityGroupJpaRepository;
    @Autowired
    AccountJpaRepository accountJpaRepository;
    @Autowired
    QuestionJpaRepository questionJpaRepository;
    @Autowired
    AuthorityJpaRepository authorityJpaRepository;

    Account account;
    AuthorityGroup authorityGroup;

    public void initData(){
        Question question = Question.builder().text("질문1").build();
        questionJpaRepository.save(question);

         account = Account.builder()
                .idString("test")
                .email("test@test.com")
                .informationOpenAgree(InformationOpenAgree.AGREE)
                .name("test")
                .nickname("test")
                .password("dasdasdasd")
                .phoneNumber("55555555555")
                .studentId("20003156")
                .type(AccountType.STUDENT)
                .question(question)
                .answer("dasdasd")
                .build();

        accountJpaRepository.save(account);

        authorityGroup = AuthorityGroup.builder()
                .name("권한그룹1")
                .description("권한설명1")
                .type(AuthorityGroupType.NORMAL)
                .build();
        authorityGroupJpaRepository.save(authorityGroup);
    }

    @Test
    void 권한그룹_사용자_생성_성공() {
        //given
        initData();
        //when
        AuthorityGroupAccountMappingReadDto.Response response = authorityGroupAccountMappingCreateUseCase.create(AuthorityGroupAccountMappingCreateDto.Request.builder()
                .accountId(account.getAccountId())
                .groupId(authorityGroup.getAuthorityGroupId())
                .build());
        //then
        AuthorityGroupAccountMapping authorityGroupAccountMapping = authorityGroupAccountMappingJpaRepository.findById(response.getId()).get();

        Assertions.assertThat(authorityGroupAccountMapping.getAccount().getIdString()).isEqualTo("test");
        Assertions.assertThat(authorityGroupAccountMapping.getAuthorityGroup().getName()).isEqualTo("권한그룹1");

        Assertions.assertThat(authorityGroupAccountMapping.getAuthorityGroup().getName()).isEqualTo(response.getGroupName());
        Assertions.assertThat(authorityGroupAccountMapping.getAuthorityGroup().getAuthorityGroupId()).isEqualTo(response.getGroupId());
        Assertions.assertThat(authorityGroupAccountMapping.getAccount().getAccountId()).isEqualTo(response.getAccountId());
        Assertions.assertThat(authorityGroupAccountMapping.getAccount().getIdString()).isEqualTo(response.getAccountIdString());
    }

    @Test
    void 권한그룹_사용자_생성_중복_실패() {
        //given
        initData();
        //when
        AuthorityGroupAccountMapping authorityGroupAccountMapping = AuthorityGroupAccountMapping.builder()
                .account(account)
                .authorityGroup(authorityGroup)
                .build();
        authorityGroupAccountMappingJpaRepository.save(authorityGroupAccountMapping);
        //then
        Assertions.assertThatThrownBy(() -> {
            authorityGroupAccountMappingCreateUseCase.create(AuthorityGroupAccountMappingCreateDto.Request.builder()
                    .accountId(account.getAccountId())
                    .groupId(authorityGroup.getAuthorityGroupId())
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(AuthorityGroupAccountMappingErrorCode.ALREADY_EXIST.getMessage());
    }
}