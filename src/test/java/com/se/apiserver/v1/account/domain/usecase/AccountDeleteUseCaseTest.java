package com.se.apiserver.v1.account.domain.usecase;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.infra.dto.AccountDeleteDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.common.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@SpringBootTest
@Transactional
public class AccountDeleteUseCaseTest {
    @Autowired
    AccountJpaRepository accountJpaRepository;

    @Autowired
    AccountDeleteUseCase accountDeleteUseCase;

    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Test
    void 회원_삭제_본인_성공() {
        //given
        createAccount();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test",
                "test", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_ACCESS"))));
        //when
        accountDeleteUseCase.delete(new AccountDeleteDto.Request("test"));
        //then
        Assertions.assertThat(accountJpaRepository.findByIdString("test").isPresent()).isEqualTo(false);
    }

    @Test
    void 회원_삭제_비회원_실패() {
        //given
        createAccount();
        //when
        //then
        Assertions.assertThatThrownBy(() -> {accountDeleteUseCase.delete(new AccountDeleteDto.Request("test"));})
                .isInstanceOf(AccessDeniedException.class).hasMessage("비정상적인 접근");
    }

    @Test
    void 회원_삭제_본인아님_실패() {
        //given
        createAccount();
        //when
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("aaa",
                "aaa", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_ACCESS"))));
        //then
        Assertions.assertThatThrownBy(() -> {accountDeleteUseCase.delete(new AccountDeleteDto.Request("test"));})
                .isInstanceOf(AccessDeniedException.class).hasMessage("비정상적인 접근");
    }


    @Test
    void 회원_삭제_관리자_성공() {
        //given
        createAccount();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("admin",
                "admin", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_MANAGE"))));
        //when
        accountDeleteUseCase.delete(new AccountDeleteDto.Request("test"));
        //then
        Assertions.assertThat(accountJpaRepository.findByIdString("test").isPresent()).isEqualTo(false);
    }

    private void createAccount() {
        Question question = Question.builder().text("질문1").build();
        questionJpaRepository.save(question);
        Account account1 = Account.builder()
                .idString("test")
                .email("test@test.com")
                .informationOpenAgree(InformationOpenAgree.AGREE)
                .name("test")
                .nickname("test")
                .password("dasdasdasd")
                .phoneNumber("55555555555")
                .studentId("10000000")
                .type(AccountType.STUDENT)
                .question(question)
                .answer("dasdasd")
                .build();
        accountJpaRepository.save(account1);
    }
}
