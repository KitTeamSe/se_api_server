package com.se.apiserver.v1.account.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.dto.AccountFindPasswordDto;
import com.se.apiserver.v1.account.application.dto.AccountFindPasswordDto.Request;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AccountFindPasswordServiceTest {
  @Mock
  private AccountJpaRepository accountJpaRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private JavaMailSender mailSender;

  @InjectMocks
  private AccountFindPasswordService accountFindPasswordService;

  private static AccountFindPasswordDto.Request request;

  @BeforeAll
  static void setup(){
    request = new Request("4글자-20글자", "test@test.com", 0L, "2글자-200글자");
  }

  @Test
  public void 회원_비밀번호_찾기_성공() throws Exception{
    //given
    when(accountJpaRepository.findByIdString(request.getId())).thenReturn(Optional.ofNullable(
        Account.builder()
            .email(request.getEmail())
            .question(Question.builder()
                .questionId(request.getQuestionId())
                .build())
            .answer(request.getAnswer())
            .build())
    );
    when(passwordEncoder.encode(anyString())).then(AdditionalAnswers.returnsFirstArg());
    //when
    //then
    assertDoesNotThrow(() -> accountFindPasswordService.findPassword(request));
  }

  @Test
  public void 회원_비밀번호_찾기_실패_이메일_불일치() throws Exception{
    //given
    when(accountJpaRepository.findByIdString(request.getId())).thenReturn(Optional.ofNullable(
        Account.builder()
            .email(request.getEmail()+"mismatch")
            .build()));
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountFindPasswordService.findPassword(request));
    //then
    assertEquals(AccountErrorCode.EMAIL_NOT_MATCH, exception.getErrorCode());
  }

  @Test
  public void 회원_비밀번호_찾기_실패_질문_불일치() throws Exception{
    //given
    when(accountJpaRepository.findByIdString(request.getId())).thenReturn(Optional.ofNullable(
        Account.builder()
            .email(request.getEmail())
            .question(Question.builder()
                .questionId(request.getQuestionId() + 1)
                .build())
            .build())
    );
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountFindPasswordService.findPassword(request));
    //then
    assertEquals(AccountErrorCode.QA_NOT_MATCH, exception.getErrorCode());
  }

  @Test
  public void 회원_비밀번호_찾기_실패_답변_불일치() throws Exception{
    //given
    when(accountJpaRepository.findByIdString(request.getId())).thenReturn(Optional.ofNullable(
        Account.builder()
            .email(request.getEmail())
            .question(Question.builder()
                .questionId(request.getQuestionId())
                .build())
            .answer(request.getAnswer()+"mismatch")
            .build())
    );
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountFindPasswordService.findPassword(request));
    //then
    assertEquals(AccountErrorCode.QA_NOT_MATCH, exception.getErrorCode());
  }
}