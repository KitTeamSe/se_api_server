package com.se.apiserver.v1.account.domain.usecase;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AccountVerifyUseCaseTest {
    @Autowired
    AccountVerifyUseCase accountVerifyUseCase;

    @Autowired
    AccountJpaRepository accountJpaRepository;

    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    Question question;
    Account account;

    private void createData() {
        question = Question.builder().text("질문1").build();
        questionJpaRepository.save(question);
        account = Account.builder()
                .idString("test")
                .email("djh10209@gmail.com")
                .informationOpenAgree(InformationOpenAgree.AGREE)
                .name("test")
                .nickname("test")
                .password(passwordEncoder.encode("test"))
                .phoneNumber("55555555555")
                .studentId("20003156")
                .type(AccountType.STUDENT)
                .question(question)
                .answer("ans")
                .build();
        accountJpaRepository.save(account);
    }

    @Test
    void 인증메일_발송요청_이메일이용_성공() {
        //given
        createData();
        //when
        boolean res = accountVerifyUseCase.sendVerifyRequestEmailByEmail("djh20@naver.com");
        //then
        Assertions.assertThat(res).isEqualTo(true);
    }

    @Test
    void 인증메일_발송요청_아이디이용_성공() {
        //given
        createData();
        //when
        boolean res = accountVerifyUseCase.sendVerifyRequestEmailByAccountId("test");
        //then
        Assertions.assertThat(res).isEqualTo(true);
    }

    @Test
    void 인증메일_발송요청_아이디없음_실패() {
        //given
        createData();
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            accountVerifyUseCase.sendVerifyRequestEmailByAccountId("test2");
        }).isInstanceOf(BusinessException.class).hasMessage(AccountErrorCode.NO_SUCH_ACCOUNT.getMessage());
    }
}