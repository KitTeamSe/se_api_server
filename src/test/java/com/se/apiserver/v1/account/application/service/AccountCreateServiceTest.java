package com.se.apiserver.v1.account.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.dto.AccountCreateDto;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.authority.application.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingCreateDto;
import com.se.apiserver.v1.authority.application.service.authoritygroup.AuthorityGroupReadService;
import com.se.apiserver.v1.authority.application.service.authoritygroupaccountmapping.AuthorityGroupAccountMappingCreateService;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AccountCreateServiceTest {
  @Mock
  private AccountJpaRepository accountJpaRepository;
  @Mock
  private QuestionJpaRepository questionJpaRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private AuthorityGroupAccountMappingCreateService authorityGroupAccountMappingCreateService;
  @Mock
  private AuthorityGroupReadService authorityGroupReadService;
  
  @InjectMocks
  private AccountCreateService accountCreateService;

  private static AccountCreateDto.Request accountCreateReqDto;
  private static String ip;

  @BeforeAll
  static void setup(){
    accountCreateReqDto = AccountCreateDto.Request.builder()
        .id("4글자-20글자")
        .password("8글자-20글자")
        .name("2글자-20글자")
        .nickname("2글자-20글자")
        .studentId("8글자-20글자")
        .type(AccountType.STUDENT)
        .phoneNumber("10글자-20글자")
        .email("test@test.com")
        .questionId(0L)
        .answer("2글자-100글자")
        .build();
    ip = "192.168.0.1";
  }

  @Test
  public void 회원가입_성공() throws Exception{
    //given
    when(accountJpaRepository.save(any(Account.class))).then(AdditionalAnswers.returnsFirstArg());
    when(questionJpaRepository.findById(accountCreateReqDto.getQuestionId()))
        .thenReturn(Optional.ofNullable(Question.builder()
            .questionId(accountCreateReqDto.getQuestionId())
            .text("test")
            .build())
        );
    when(passwordEncoder.encode(accountCreateReqDto.getPassword())).then(AdditionalAnswers.returnsFirstArg());
    when(authorityGroupReadService.getDefaultAuthorityGroup()).thenReturn(new AuthorityGroup("default", "default for test", AuthorityGroupType.DEFAULT));
    when(authorityGroupAccountMappingCreateService.create(any(AuthorityGroupAccountMappingCreateDto.Request.class))).thenReturn(null);
    //when
    //then
    assertDoesNotThrow(() -> {
      accountCreateService.signUp(accountCreateReqDto, ip);
    });
  }

  @Test
  public void 회원가입_실패_닉네임_중복() throws Exception{
    //given
    when(accountJpaRepository.findByNickname(accountCreateReqDto.getNickname())).thenReturn(Optional.ofNullable(Account.builder().build()));
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountCreateService.signUp(accountCreateReqDto, ip));
    //then
    assertEquals(AccountErrorCode.DUPLICATED_NICKNAME, exception.getErrorCode());
  }
  
  @Test
  public void 회원가입_실패_ID_중복() throws Exception{
    //given
    when(accountJpaRepository.findByIdString(accountCreateReqDto.getId())).thenReturn(Optional.ofNullable(Account.builder().build()));
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountCreateService.signUp(accountCreateReqDto, ip));
    //then
    assertEquals(AccountErrorCode.DUPLICATED_ID, exception.getErrorCode());
  }
  
  @Test
  public void 회원가입_실패_학번_중복() throws Exception{
    //given
    when(accountJpaRepository.findByStudentId(accountCreateReqDto.getStudentId())).thenReturn(Optional.ofNullable(Account.builder().build()));
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountCreateService.signUp(accountCreateReqDto, ip));
    //then
    assertEquals(AccountErrorCode.DUPLICATED_STUDENT_ID, exception.getErrorCode());
  }
  
  @Test
  public void 회원가입_실패_이메일_중복() throws Exception{
    //given
    when(accountJpaRepository.findByEmail(accountCreateReqDto.getEmail())).thenReturn(Optional.ofNullable(Account.builder().build()));
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountCreateService.signUp(accountCreateReqDto, ip));
    //then
    assertEquals(AccountErrorCode.DUPLICATED_EMAIL, exception.getErrorCode());
  }

  @Test
  public void 회원가입_실패_질문_없음() throws Exception{
    //given
    when(questionJpaRepository.findById(accountCreateReqDto.getQuestionId())).thenReturn(Optional.ofNullable(null));
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountCreateService.signUp(accountCreateReqDto, ip));
    //then
    assertEquals(AccountErrorCode.NO_SUCH_QUESTION, exception.getErrorCode());
  }
}