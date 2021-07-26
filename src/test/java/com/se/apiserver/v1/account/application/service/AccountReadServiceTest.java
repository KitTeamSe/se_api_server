package com.se.apiserver.v1.account.application.service;

import com.se.apiserver.v1.account.application.dto.AccountReadDto.AccountSearchRequest;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.application.dto.AccountReadDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@SpringBootTest
@Transactional
public class AccountReadServiceTest {

    @Autowired
    AccountReadService accountReadService;
    @Autowired
    AccountJpaRepository accountJpaRepository;
    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Test
    public void 본인_조회_성공() {
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

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(String.valueOf(account.getAccountId()),
                "3", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_ACCESS"))));

        //when
        AccountReadDto.Response dto = accountReadService.read("test");
        //then
        Account res = accountJpaRepository.findByIdString("test").get();

        Assertions.assertThat(dto.getLastSignInIp()).isEqualTo(null);
        Assertions.assertThat(dto.getAccountId()).isEqualTo(account.getAccountId());
        Assertions.assertThat(dto.getStudentId()).isEqualTo(res.getStudentId());
        Assertions.assertThat(dto.getPhoneNumber()).isEqualTo(res.getPhoneNumber());
        Assertions.assertThat(dto.getInformationOpenAgree()).isEqualTo(res.getInformationOpenAgree());
        Assertions.assertThat(dto.getIdString()).isEqualTo(res.getIdString());
    }

    @Test
    public void 비로그인_조회_실패() {
        Assertions.assertThatThrownBy(() -> {
            accountReadService.read("user");
        }).isInstanceOf(AccessDeniedException.class);
    }

    @Transactional
    @Test
    public void 로그인_본인아님_관리자아님_조회_미포함정보() {
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

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test",
                "test", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_ACCESS"))));

        //when
        AccountReadDto.Response dto = accountReadService.read("user");

        Assertions.assertThat(dto.getLastSignInIp()).isEqualTo(null);
        Assertions.assertThat(dto.getAccountId()).isEqualTo(null);
        Assertions.assertThat(dto.getStudentId()).isEqualTo(null);
        Assertions.assertThat(dto.getPhoneNumber()).isEqualTo(null);
        Assertions.assertThat(dto.getInformationOpenAgree()).isEqualTo(null);
        Assertions.assertThat(dto.getIdString()).isEqualTo("user");
    }


    //sql로 생성한 계정 2개
    @Test
    void 회원_검색_페이지() {
        //given
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("1",
            "1", Arrays.asList(new SimpleGrantedAuthority("ACCOUNT_ACCESS"))));
        //when
        PageImpl search = accountReadService.search(AccountSearchRequest.builder()
                .pageRequest(PageRequest.builder().page(1).size(10).direction(Sort.Direction.ASC).build())
                .build());
        //then
        Assertions.assertThat(search.getTotalElements()).isEqualTo(2);
    }
}
