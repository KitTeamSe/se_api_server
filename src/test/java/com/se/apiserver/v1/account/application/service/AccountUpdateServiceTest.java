package com.se.apiserver.v1.account.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.dto.AccountUpdateDto;
import com.se.apiserver.v1.account.application.dto.AccountUpdateDto.Request;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AccountUpdateServiceTest {
  @Mock
  private AccountJpaRepository accountJpaRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private QuestionJpaRepository questionJpaRepository;
  @Mock
  private AccountContextService accountContextService;

  @InjectMocks
  private AccountUpdateService accountUpdateService;

  @BeforeAll
  static void setup(){
  }

  @Test
  public void 회원_업데이트_성공_회원_ALL() throws Exception{
    //given
    AccountUpdateDto.Request request = AccountUpdateDto.Request.builder()
        .id("4글자-20글자")
        .password("8글자-20글자")
        .name("2글자-20글자")
        .nickname("2글자-20글자")
        .type(AccountType.STUDENT)
        .informationOpenAgree(InformationOpenAgree.AGREE)
        .questionId(0L)
        .answer("2글자-100글자")
        .build();
    when(accountJpaRepository.findByIdString(anyString())).thenReturn(
        Optional.ofNullable(Account.builder().build())
    );
    when(accountContextService.isOwner(any(Account.class))).thenReturn(true);
    when(passwordEncoder.encode(anyString())).then(AdditionalAnswers.returnsFirstArg());
    when(questionJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(Question.builder().build()));
    //when
    //then
    assertDoesNotThrow(() -> accountUpdateService.update(request));
  }

  @Test
  public void 회원_업데이트_성공_관리자_ALL() throws Exception{
    //given
    AccountUpdateDto.Request request = AccountUpdateDto.Request.builder()
        .id("4글자-20글자")
        .password("8글자-20글자")
        .name("2글자-20글자")
        .nickname("2글자-20글자")
        .type(AccountType.ASSISTANT)
        .informationOpenAgree(InformationOpenAgree.AGREE)
        .questionId(0L)
        .answer("2글자-100글자")
        .build();
    when(accountJpaRepository.findByIdString(anyString())).thenReturn(
        Optional.ofNullable(Account.builder().build())
    );
    when(accountContextService.hasAuthority(anyString())).thenReturn(true);
    when(passwordEncoder.encode(anyString())).then(AdditionalAnswers.returnsFirstArg());
    when(questionJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(Question.builder().build()));
    //when
    //then
    assertDoesNotThrow(() -> accountUpdateService.update(request));
  }

  @Test
  public void 회원_업데이트_성공_변경_없음() throws Exception{
    //given
    AccountUpdateDto.Request request = AccountUpdateDto.Request.builder()
        .id("4글자-20글자")
        .build();
    when(accountJpaRepository.findByIdString(anyString())).thenReturn(
        Optional.ofNullable(Account.builder().build())
    );
    when(accountContextService.isOwner(any(Account.class))).thenReturn(true);
    //when
    //then
    assertDoesNotThrow(() -> accountUpdateService.update(request));
  }

  @Test
  public void 회원_업데이트_실패_ID_불일치() throws Exception{
    //given
    AccountUpdateDto.Request request = Request.builder().id("4글자-20글자").build();
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountUpdateService.update(request));
    //then
    assertEquals(AccountErrorCode.NO_SUCH_ACCOUNT, exception.getErrorCode());
  }

  @Test
  public void 회원_업데이트_실패_권한_없음() throws Exception{
    //given
    AccountUpdateDto.Request request = Request.builder().id("4글자-20글자").build();
    when(accountJpaRepository.findByIdString(anyString())).thenReturn(Optional.ofNullable(Account.builder().build()));
    //when
    AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> accountUpdateService.update(request));
    //then
    assertEquals("비정상적인 접근", exception.getMessage());
  }

  @Test
  public void 회원_업데이트_실패_QNA_비정상() throws Exception{
    //given
    AccountUpdateDto.Request request = Request.builder().id("4글자-20글자").questionId(0L).build();
    when(accountJpaRepository.findByIdString(anyString())).thenReturn(Optional.ofNullable(Account.builder().build()));
    when(accountContextService.isOwner(any(Account.class))).thenReturn(true);
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountUpdateService.update(request));
    //then
    assertEquals(AccountErrorCode.QNA_INVALID_INPUT, exception.getErrorCode());
  }

  @Test
  public void 회원_업데이트_실패_질문_불일치() throws Exception{
    //given
    AccountUpdateDto.Request request = AccountUpdateDto.Request.builder()
        .id("4글자-20글자")
        .password("8글자-20글자")
        .name("2글자-20글자")
        .nickname("2글자-20글자")
        .type(AccountType.ASSISTANT)
        .informationOpenAgree(InformationOpenAgree.AGREE)
        .questionId(0L)
        .answer("2글자-100글자")
        .build();
    when(accountJpaRepository.findByIdString(anyString())).thenReturn(
        Optional.ofNullable(Account.builder().build())
    );
    when(accountContextService.hasAuthority(anyString())).thenReturn(true);
    when(passwordEncoder.encode(anyString())).then(AdditionalAnswers.returnsFirstArg());
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountUpdateService.update(request));
    //then
    assertEquals(AccountErrorCode.NO_SUCH_QUESTION, exception.getErrorCode());
  }

  @Test
  public void 회원_업데이트_실패_닉네임_중복() throws Exception{
    //given
    AccountUpdateDto.Request request = AccountUpdateDto.Request.builder()
        .id("4글자-20글자")
        .password("8글자-20글자")
        .name("2글자-20글자")
        .nickname("2글자-20글자")
        .type(AccountType.ASSISTANT)
        .informationOpenAgree(InformationOpenAgree.AGREE)
        .build();
    long accountId = 0L;
    when(accountJpaRepository.findByIdString(anyString())).thenReturn(
        Optional.ofNullable(Account.builder().accountId(accountId).build())
    );
    when(accountContextService.hasAuthority(anyString())).thenReturn(true);
    when(passwordEncoder.encode(anyString())).then(AdditionalAnswers.returnsFirstArg());
    when(accountJpaRepository.findByNickname(anyString())).thenReturn(Optional.ofNullable(Account.builder()
        .accountId(accountId + 1)
        .build())
    );
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountUpdateService.update(request));
    //then
    assertEquals(AccountErrorCode.DUPLICATED_NICKNAME, exception.getErrorCode());
  }
}