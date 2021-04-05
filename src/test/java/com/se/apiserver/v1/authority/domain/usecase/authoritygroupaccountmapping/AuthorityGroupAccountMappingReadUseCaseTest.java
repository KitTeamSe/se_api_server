package com.se.apiserver.v1.authority.domain.usecase.authoritygroupaccountmapping;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.authority.infra.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingReadDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupAccountMappingJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityGroupJpaRepository;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthorityGroupAccountMappingReadUseCaseTest {
    @Autowired
    AuthorityGroupAccountMappingReadUseCase authorityGroupAccountMappingReadUseCase;
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

    Account account1;
    Account account2;
    AuthorityGroup authorityGroup;

    public void initData(){
        Question question = Question.builder().text("질문1").build();
        questionJpaRepository.save(question);

        account1 = Account.builder()
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

        accountJpaRepository.save(account1);

        account2 = Account.builder()
                .idString("test2")
                .email("tes2t@test.com")
                .informationOpenAgree(InformationOpenAgree.AGREE)
                .name("test2")
                .nickname("test2")
                .password("dasdasdasd2")
                .phoneNumber("555555555552")
                .studentId("20003152")
                .type(AccountType.STUDENT)
                .question(question)
                .answer("dasdasd2")
                .build();

        accountJpaRepository.save(account2);

        authorityGroup = new AuthorityGroup("권한그룹1"," 권한설명1", AuthorityGroupType.NORMAL);
        authorityGroupJpaRepository.save(authorityGroup);
    }

    @Test
    void 권한그룹_사용자_조회_성공() {
        //given
        initData();
        AuthorityGroupAccountMapping authorityGroupAccountMapping = new AuthorityGroupAccountMapping(account1,authorityGroup);
        authorityGroupAccountMappingJpaRepository.save(authorityGroupAccountMapping);
        //when
        AuthorityGroupAccountMappingReadDto.Response read = authorityGroupAccountMappingReadUseCase.read(authorityGroupAccountMapping.getAuthorityGroupAccountMappingId());
        //then
        Assertions.assertThat(read.getAccountId()).isEqualTo(account1.getAccountId());
        Assertions.assertThat(read.getAccountIdString()).isEqualTo(account1.getIdString());
        Assertions.assertThat(read.getGroupId()).isEqualTo(authorityGroup.getAuthorityGroupId());
        Assertions.assertThat(read.getGroupName()).isEqualTo(authorityGroup.getName());
        Assertions.assertThat(read.getId()).isEqualTo(authorityGroupAccountMapping.getAuthorityGroupAccountMappingId());
    }

    @Test
    void 권한그룹_사용자_목록_조회_성공() {
        //given
        Long exist = authorityGroupAccountMappingJpaRepository.count();
        initData();
        AuthorityGroupAccountMapping authorityGroupAccountMapping1 =  new AuthorityGroupAccountMapping(account1,authorityGroup);
        authorityGroupAccountMappingJpaRepository.save(authorityGroupAccountMapping1);
        AuthorityGroupAccountMapping authorityGroupAccountMapping2 = new AuthorityGroupAccountMapping(account2,authorityGroup);
        authorityGroupAccountMappingJpaRepository.save(authorityGroupAccountMapping2);
        //when
        PageImpl page = authorityGroupAccountMappingReadUseCase.readAll(PageRequest.builder()
                .direction(Sort.Direction.ASC)
                .size(10)
                .page(1)
                .build().of());
        //then
        Assertions.assertThat(page.getTotalElements()).isEqualTo(exist + 2);
    }
}