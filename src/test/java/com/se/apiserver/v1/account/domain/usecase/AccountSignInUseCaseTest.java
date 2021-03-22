package com.se.apiserver.v1.account.domain.usecase;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.dto.AccountSignInDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.common.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountSignInUseCaseTest {
    @Autowired
    AccountSignInUseCase accountSignInUseCase;
    @Autowired
    AccountJpaRepository accountJpaRepository;
    @Autowired
    QuestionJpaRepository questionJpaRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
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
    void 로그인_성공() {
        //given
        createAccount();
        //when
        AccountSignInDto.Response response = accountSignInUseCase.signIn("test", "test");
        //then
        Assertions.assertThat(response.getToken()).isNotEmpty();
    }

    @Test
    void 로그인_비밀번호틀림_실패() {
        //given
        createAccount();
        //when
        //then
        Assertions.assertThatThrownBy(()-> {accountSignInUseCase.signIn("test", "test2222");})
                .isInstanceOf(BusinessException.class).hasMessage(AccountErrorCode.PASSWORD_INCORRECT.getMessage());
    }
}