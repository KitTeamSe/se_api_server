package com.se.apiserver.v1.account.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.dto.AccountSignInDto;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.authority.application.service.authoritygroup.AuthorityGroupReadService;
import com.se.apiserver.v1.authority.application.service.authoritygroupaccountmapping.AuthorityGroupAccountMappingReadService;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.common.domain.error.GlobalErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.security.provider.JwtTokenResolver;
import java.util.Collections;
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
class AccountSignInServiceTest {

  @Mock
  private JwtTokenResolver jwtTokenResolver;
  @Mock
  private AccountJpaRepository accountJpaRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private AuthorityGroupReadService authorityGroupReadService;
  @Mock
  private AuthorityGroupAccountMappingReadService authorityGroupAccountMappingReadService;

  @InjectMocks
  private AccountSignInService accountSignInService;

  private static String id;
  private static String password;
  private static String ip;

  @BeforeAll
  static void setup(){
    id = "4글자-20글자";
    password = "password";
    ip = "192.168.0.1";
  }

  @Test
  public void 로그인_성공_회원() throws Exception{
    //given
    Long accountId = 12345678L;
    when(accountJpaRepository.findByIdString(anyString())).thenReturn(
        Optional.ofNullable(Account.builder()
            .accountId(accountId)
            .password(password)
            .build())
    );
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    when(jwtTokenResolver.createToken(anyString())).then(AdditionalAnswers.returnsFirstArg());
    //when
    AccountSignInDto.Response response = accountSignInService.signIn(id, password, ip);
    //then
    assertEquals(String.valueOf(accountId), response.getToken());
  }

  @Test
  public void 로그인_실패_회원_ID_불일치() throws Exception{
    //given

    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountSignInService.signIn(id, password, ip));
    //then
    assertEquals(AccountErrorCode.NO_SUCH_ACCOUNT, exception.getErrorCode());
  }

  @Test
  public void 로그인_실패_회원_PW_불일치() throws Exception{
    //given
    when(accountJpaRepository.findByIdString(anyString())).thenReturn(
        Optional.ofNullable(Account.builder()
            .password(password)
            .build())
    );
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountSignInService.signIn(id, password, ip));
    //then
    assertEquals(AccountErrorCode.PASSWORD_INCORRECT, exception.getErrorCode());
  }

  @Test
  public void 로그인_성공_관리자() throws Exception{
    //given
    Long accountId = 12345678L;
    when(accountJpaRepository.findByIdString(anyString())).thenReturn(
        Optional.ofNullable(Account.builder()
            .accountId(accountId)
            .password(password)
            .build())
    );
    when(authorityGroupReadService.getAuthorityGroupsByType(AuthorityGroupType.SYSTEM)).thenReturn(
        Collections.singletonList(new AuthorityGroup("2글자-30글자", "2글자-100글자", AuthorityGroupType.SYSTEM)));
    when(authorityGroupAccountMappingReadService.isAccountMappedToGroup(anyLong(), any(AuthorityGroup.class))).thenReturn(true);
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    when(jwtTokenResolver.createToken(anyString())).then(AdditionalAnswers.returnsFirstArg());
    //when
    AccountSignInDto.Response response = accountSignInService.signInAsManager(id, password, ip);
    //then
    assertEquals(String.valueOf(accountId), response.getToken());
  }

  @Test
  public void 로그인_실패_관리자_ID_불일치() throws Exception{
    //given

    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountSignInService.signInAsManager(id, password, ip));
    //then
    assertEquals(AccountErrorCode.NO_SUCH_ACCOUNT, exception.getErrorCode());
  }

  @Test
  public void 로그인_실패_관리자_권한_없음() throws Exception{
    //given
    when(accountJpaRepository.findByIdString(anyString())).thenReturn(
        Optional.ofNullable(Account.builder()
            .password(password)
            .build())
    );
    when(authorityGroupReadService.getAuthorityGroupsByType(AuthorityGroupType.SYSTEM)).thenReturn(
        Collections.singletonList(new AuthorityGroup("2글자-30글자", "2글자-100글자", AuthorityGroupType.SYSTEM))
    );
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountSignInService.signInAsManager(id, password, ip));
    //then
    assertEquals(GlobalErrorCode.HANDLE_ACCESS_DENIED, exception.getErrorCode());
  }

  @Test
  public void 로그인_실패_관리자_PW_불일치() throws Exception{
    //given
    Long accountId = 12345678L;
    when(accountJpaRepository.findByIdString(anyString())).thenReturn(
        Optional.ofNullable(Account.builder()
            .accountId(accountId)
            .password(password)
            .build())
    );
    when(authorityGroupReadService.getAuthorityGroupsByType(AuthorityGroupType.SYSTEM)).thenReturn(
        Collections.singletonList(new AuthorityGroup("2글자-30글자", "2글자-100글자", AuthorityGroupType.SYSTEM))
    );
    when(authorityGroupAccountMappingReadService.isAccountMappedToGroup(anyLong(), any(AuthorityGroup.class))).thenReturn(true);
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountSignInService.signInAsManager(id, password, ip));
    //then
    assertEquals(AccountErrorCode.PASSWORD_INCORRECT, exception.getErrorCode());
  }
}