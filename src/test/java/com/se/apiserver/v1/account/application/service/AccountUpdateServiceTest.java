package com.se.apiserver.v1.account.application.service;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.application.dto.AccountUpdateDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@SpringBootTest
@Transactional
class AccountUpdateServiceTest {

    @Autowired
    AccountUpdateService accountUpdateService;

    @Autowired
    AccountJpaRepository accountJpaRepository;

    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    Question question;
    Question question2;
    Account account;

    private void createData() {
        question = Question.builder().text("질문1").build();
        question2 = Question.builder().text("질문2").build();
        questionJpaRepository.save(question);
        questionJpaRepository.save(question2);
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
    void 업데이트_성공() {
        //given
        createData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("1",
                "1", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_ACCESS"))));
        //when
        accountUpdateService.update(
                AccountUpdateDto.Request.builder()
                        .id("user")
                        .nickname("newwww")
                        .studentId("20005555")
                        .questionId(question2.getQuestionId())
                        .answer("dasdasdas")
                        .build()
        );
        //then
        account = accountJpaRepository.findById(1L).get();
        Assertions.assertThat(account.getNickname()).isEqualTo("newwww");
        Assertions.assertThat(account.getStudentId()).isEqualTo("20005555");
        Assertions.assertThat(account.getQuestion().getQuestionId()).isEqualTo(question2.getQuestionId());
        Assertions.assertThat(account.getAnswer()).isEqualTo("dasdasdas");
    }

    @Test
    void 업데이트_관리자_성공() {
        //given
        createData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("2",
                "2", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_MANAGE"))));
        //when
        accountUpdateService.update(
                AccountUpdateDto.Request.builder()
                        .id("admin")
                        .nickname("newwww")
                        .studentId("20005555")
                        .questionId(question2.getQuestionId())
                        .answer("dasdasdas")
                        .build()
        );
        //then
        account = accountJpaRepository.findById(2L).get();
        Assertions.assertThat(account.getNickname()).isEqualTo("newwww");
        Assertions.assertThat(account.getStudentId()).isEqualTo("20005555");
        Assertions.assertThat(account.getQuestion().getQuestionId()).isEqualTo(question2.getQuestionId());
        Assertions.assertThat(account.getAnswer()).isEqualTo("dasdasdas");
    }

    @Test
    void 업데이트_닉네임중복_실패() {
        //given
        createData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("2",
                "test2", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_MANAGE"))));
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            accountUpdateService.update(
                    AccountUpdateDto.Request.builder()
                            .id("test")
                            .nickname("test")
                            .studentId("20005555")
                            .questionId(question2.getQuestionId())
                            .answer("dasdasdas")
                            .build());
        }).isInstanceOf(BusinessException.class).hasMessage(AccountErrorCode.DUPLICATED_NICKNAME.getMessage());
    }

    @Test
    void 업데이트_학번중복_실패() {
        //given
        createData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("1",
                "1", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_MANAGE"))));
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            accountUpdateService.update(
                    AccountUpdateDto.Request.builder()
                            .id("user")
                            .nickname("test2")
                            .studentId("20003156")
                            .questionId(question2.getQuestionId())
                            .answer("dasdasdas")
                            .build());
        }).isInstanceOf(BusinessException.class).hasMessage(AccountErrorCode.DUPLICATED_STUDENT_ID.getMessage());
    }

    @Test
    void 업데이트_질문없음_실패() {
        //given
        createData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test2",
                "test2", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_MANAGE"))));
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            accountUpdateService.update(
                    AccountUpdateDto.Request.builder()
                            .id("test")
                            .nickname("test2")
                            .studentId("20003156")
                            .answer("dasdasdas")
                            .build());
        }).isInstanceOf(BusinessException.class).hasMessage(AccountErrorCode.QNA_INVALID_INPUT.getMessage());
    }

    @Test
    void 업데이트_대답없음_실패() {
        //given
        createData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test2",
                "test2", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_MANAGE"))));
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            accountUpdateService.update(
                    AccountUpdateDto.Request.builder()
                            .id("test")
                            .nickname("test2")
                            .studentId("20003156")
                            .questionId(question2.getQuestionId())
                            .build());
        }).isInstanceOf(BusinessException.class).hasMessage(AccountErrorCode.QNA_INVALID_INPUT.getMessage());
    }

    @Test
    void 업데이트_비로그인_실패() {
        //given
        createData();
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            accountUpdateService.update(
                    AccountUpdateDto.Request.builder()
                            .id("test")
                            .nickname("newwww")
                            .studentId("20005555")
                            .questionId(question2.getQuestionId())
                            .answer("dasdasdas")
                            .build());
        }).isInstanceOf(AccessDeniedException.class).hasMessage("비정상적인 접근");
    }
}