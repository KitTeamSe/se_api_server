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
import com.se.apiserver.v1.authority.domain.error.AuthorityGroupAccountMappingErrorCode;
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
class AuthorityGroupAccountMappingDeleteUseCaseTest {
    @Autowired
    AuthorityGroupAccountMappingDeleteUseCase authorityGroupAccountMappingDeleteUseCase;
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

        authorityGroup = new AuthorityGroup("권한그룹1", "권한설명1", AuthorityGroupType.NORMAL);
        authorityGroupJpaRepository.save(authorityGroup);
    }

    @Test
    void 삭제_성공() {
        //given
        initData();
        AuthorityGroupAccountMapping authorityGroupAccountMapping = new AuthorityGroupAccountMapping(account,authorityGroup);
        AuthorityGroupAccountMapping save = authorityGroupAccountMappingJpaRepository.save(authorityGroupAccountMapping);
        Long id = save.getAuthorityGroupAccountMappingId();
        //when
        authorityGroupAccountMappingDeleteUseCase.delete(id);
        //then
        Assertions.assertThat(authorityGroupJpaRepository.findById(id).isEmpty()).isEqualTo(true);
    }

    @Test
    void 삭제_실패_미존재() {
        //given
        initData();
        AuthorityGroupAccountMapping authorityGroupAccountMapping = new AuthorityGroupAccountMapping(account,authorityGroup);
        AuthorityGroupAccountMapping save = authorityGroupAccountMappingJpaRepository.save(authorityGroupAccountMapping);
        Long id = save.getAuthorityGroupAccountMappingId();
        //when
        //then
        Assertions.assertThatThrownBy(()-> {
            authorityGroupAccountMappingDeleteUseCase.delete(id+1);
        }).isInstanceOf(BusinessException.class).hasMessage(AuthorityGroupAccountMappingErrorCode.NO_SUCH_MAPPING.getMessage());
    }
}