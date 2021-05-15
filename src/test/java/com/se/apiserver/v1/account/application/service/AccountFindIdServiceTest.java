package com.se.apiserver.v1.account.application.service;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.application.dto.AccountFindIdByEmailDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AccountFindIdServiceTest {
    @Autowired
    AccountFindIdService accountFindIdService;
    @Autowired
    AccountJpaRepository accountJpaRepository;
    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Test
    void 아이디_찾기_성공() {
        //given
        Question question = Question.builder().text("질문1").build();
        questionJpaRepository.save(question);
        Account account = Account.builder()
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
        //when
        AccountFindIdByEmailDto.Response response = accountFindIdService.readByEmail("test@test.com");
        //then
        Assertions.assertThat(response.getId()).isEqualTo(accountFindIdService.hideLastTwoCharacter(account.getIdString()));
    }

    @Test
    void 아이디_찾기_실패() {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            accountFindIdService.readByEmail("test@test.com");})
                .isInstanceOf(BusinessException.class).hasMessage(AccountErrorCode.NO_SUCH_ACCOUNT.getMessage());
    }
}