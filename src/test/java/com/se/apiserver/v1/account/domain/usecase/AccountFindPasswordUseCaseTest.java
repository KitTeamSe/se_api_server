package com.se.apiserver.v1.account.domain.usecase;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.dto.AccountFindPasswordDto;
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
class AccountFindPasswordUseCaseTest {
    @Autowired
    AccountFindPasswordUseCase accountFindPasswordUseCase;
    @Autowired
    AccountJpaRepository accountJpaRepository;
    @Autowired
    QuestionJpaRepository questionJpaRepository;

    Question question;
    Account account;

    private void createAccount() {
        question = Question.builder().text("질문1").build();
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
                .answer("ans")
                .build();
        accountJpaRepository.save(account);
    }

    @Test
    void 비밀번호_찾기_성공() {
        //given
        createAccount();
        //when
        boolean res = accountFindPasswordUseCase.findPassword(new AccountFindPasswordDto.Request("test", "test@test.com", question.getQuestionId(),
                "ans"));
        //then
        Assertions.assertThat(res).isEqualTo(true);
    }

    @Test
    void 비밀번호_찾기_이메일틀림_실패() {
        //given
        createAccount();
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            accountFindPasswordUseCase.findPassword(new AccountFindPasswordDto.Request("test", "test2@test.com", question.getQuestionId(),
                    "ans"));
        }).isInstanceOf(BusinessException.class).hasMessage(AccountErrorCode.EMAIL_NOT_MATCH.getMessage());
    }

    @Test
    void 비밀번호_찾기_답변틀림_실패() {
        //given
        createAccount();
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            accountFindPasswordUseCase.findPassword(new AccountFindPasswordDto.Request("test", "test@test.com", question.getQuestionId(),
                    "ansdasdasds"));
        }).isInstanceOf(BusinessException.class).hasMessage(AccountErrorCode.QA_NOT_MATCH.getMessage());
    }

    @Test
    void 비밀번호_찾기_질문틀림_실패() {
        //given
        createAccount();
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            accountFindPasswordUseCase.findPassword(new AccountFindPasswordDto.Request("test", "test@test.com", question.getQuestionId()+2,
                    "ans"));
        }).isInstanceOf(BusinessException.class).hasMessage(AccountErrorCode.QA_NOT_MATCH.getMessage());
    }
}