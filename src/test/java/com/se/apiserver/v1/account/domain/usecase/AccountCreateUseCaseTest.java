package com.se.apiserver.v1.account.domain.usecase;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.dto.AccountCreateDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.common.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class AccountCreateUseCaseTest {
    @Autowired
    AccountJpaRepository accountJpaRepository;

    @Autowired
    AccountCreateUseCase accountCreateUseCase;

    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Test
    void 회원_등록_성공() {
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
        //when
        accountJpaRepository.save(account);
        //then
        Account res = accountJpaRepository.findByIdString("test").get();

        Assertions.assertThat(res.getAccountId()).isNotEqualTo(null);
        Assertions.assertThat(res.getStudentId()).isEqualTo("20003156");
        Assertions.assertThat(res.getType()).isEqualTo(AccountType.STUDENT);
    }

    @Test
    void 회원_등록_닉네임중복_실패() {
        //given
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
        //when
        AccountCreateDto.Request req = AccountCreateDto.Request.builder()
                .id("test2")
                .email("test2@test.com")
                .name("test2")
                .nickname("test")
                .password("dasdasdasd")
                .phoneNumber("55555555555")
                .studentId("20000000")
                .type(AccountType.STUDENT)
                .questionId(question.getQuestionId())
                .answer("dasdasd")
                .build();
        //then
        Assertions.assertThatThrownBy(() -> {accountCreateUseCase.signUp(req, "192.168.0.1");})
                .isInstanceOf(BusinessException.class).hasMessage(AccountErrorCode.DUPLICATED_NICKNAME.getMessage());
    }

    @Test
    void 회원_등록_학번_중복_실패() {
        //given
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
                .studentId("20000000")
                .type(AccountType.STUDENT)
                .question(question)
                .answer("dasdasd")
                .build();
        accountJpaRepository.save(account1);
        //when
        AccountCreateDto.Request req = AccountCreateDto.Request.builder()
                .id("test2")
                .email("test2@test.com")
                .name("test2")
                .nickname("test2")
                .password("dasdasdasd")
                .phoneNumber("55555555555")
                .studentId("20000000")
                .type(AccountType.STUDENT)
                .questionId(question.getQuestionId())
                .answer("dasdasd")
                .build();
        //then
        Assertions.assertThatThrownBy(() -> {accountCreateUseCase.signUp(req, "192.168.0.1");})
                .isInstanceOf(BusinessException.class).hasMessage(AccountErrorCode.DUPLICATED_STUDENT_ID.getMessage());
    }

    @Test
    void 회원_등록_이메일_중복_실패() {
        //given
        Question question = Question.builder().text("질문1").build();
        questionJpaRepository.save(question);

        Account account1 = Account.builder()
                .idString("test")
                .email("test2@test.com")
                .informationOpenAgree(InformationOpenAgree.AGREE)
                .name("test")
                .nickname("test")
                .password("dasdasdasd")
                .phoneNumber("55555555555")
                .studentId("20000000")
                .type(AccountType.STUDENT)
                .question(question)
                .answer("dasdasd")
                .build();
        accountJpaRepository.save(account1);
        //when
        AccountCreateDto.Request req = AccountCreateDto.Request.builder()
                .id("test2")
                .email("test2@test.com")
                .name("test2")
                .nickname("test2")
                .password("dasdasdasd")
                .phoneNumber("55555555555")
                .studentId("20000001")
                .type(AccountType.STUDENT)
                .questionId(question.getQuestionId())
                .answer("dasdasd")
                .build();
        //then
        Assertions.assertThatThrownBy(() -> {accountCreateUseCase.signUp(req, "192.168.0.1");})
                .isInstanceOf(BusinessException.class).hasMessage(AccountErrorCode.DUPLICATED_EMAIL.getMessage());
    }

    @Test
    void 회원_등록_아이디_중복_실패() {
        //given
        Question question = Question.builder().text("질문1").build();
        questionJpaRepository.save(question);

        Account account1 = Account.builder()
                .idString("test")
                .email("test1@test.com")
                .informationOpenAgree(InformationOpenAgree.AGREE)
                .name("test")
                .nickname("test")
                .password("dasdasdasd")
                .phoneNumber("55555555555")
                .studentId("20000000")
                .type(AccountType.STUDENT)
                .question(question)
                .answer("dasdasd")
                .build();
        accountJpaRepository.save(account1);
        //when
        AccountCreateDto.Request req = AccountCreateDto.Request.builder()
                .id("test")
                .email("test2@test.com")
                .name("test2")
                .nickname("test2")
                .password("dasdasdasd")
                .phoneNumber("55555555555")
                .studentId("20000001")
                .type(AccountType.STUDENT)
                .questionId(question.getQuestionId())
                .answer("dasdasd")
                .build();
        //then
        Assertions.assertThatThrownBy(() -> {accountCreateUseCase.signUp(req, "192.168.0.1");})
                .isInstanceOf(BusinessException.class).hasMessage(AccountErrorCode.DUPLICATED_ID.getMessage());
    }


}
