package com.se.apiserver.v1.account.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@ExtendWith(MockitoExtension.class)
class AccountContextServiceTest {
  @Mock
  private AccountJpaRepository accountJpaRepository;
  @Mock
  private AuthorityJpaRepository authorityJpaRepository;

  @InjectMocks
  private AccountContextService accountContextService;

  private static long accountId;
  private static String password;
  private static List<Authority> authorities;
  private static User user;
  private static UsernamePasswordAuthenticationToken authentication;


  @BeforeAll
  static void setup(){
    accountId = 0L;
    password = "password";
    authorities = Collections.singletonList(new Authority("2글자-20글자", "2글자-20글자"));
    user = new User(String.valueOf(accountId), password, new HashSet<>(authorities));
    authentication = new UsernamePasswordAuthenticationToken(user,null);
  }

  @Test
  public void loadUser_성공() throws Exception{
    //given
    when(accountJpaRepository.findById(anyLong())).thenReturn(
        Optional.ofNullable(Account.builder().accountId(accountId).password(password).build()));
    when(authorityJpaRepository.findByAccountId(anyLong())).thenReturn(authorities);
    //when

    User user = (User) accountContextService.loadUserByUsername(String.valueOf(accountId));
    //then
    assertAll(
        () -> assertEquals(String.valueOf(accountId), user.getUsername()),
        () -> assertEquals(password, user.getPassword()),
        () -> assertIterableEquals(authorities, user.getAuthorities())
    );
  }

  @Test
  public void loadUser_실패_ID_불일치() throws Exception{
    //given
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountContextService.loadUserByUsername(String.valueOf(accountId)));
    //then
    assertEquals(AccountErrorCode.NO_SUCH_ACCOUNT, exception.getErrorCode());
  }

  @Test
  public void getContextAccount_성공() throws Exception{
    //given
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(accountJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(
        Account.builder().accountId(accountId).build())
    );
    //when
    Account account = accountContextService.getContextAccount();
    //then
    assertEquals(accountId, account.getAccountId());
  }

  @Test
  public void getContextAccount_실패_ID_불일치() throws Exception{
    //given
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountContextService.getContextAccount());
    //then
    assertEquals(AccountErrorCode.NO_SUCH_ACCOUNT, exception.getErrorCode());
  }

  @Test
  public void 권한_검증() throws Exception{
    //given
    String authTrue = authorities.get(0).getNameEng(), authFalse = "false";
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    //when
    //then
    assertAll(
        () -> assertDoesNotThrow(() -> accountContextService.hasAuthority(authTrue)),
        () -> assertDoesNotThrow(() -> accountContextService.hasAuthority(authFalse))
    );
  }

  @Test
  public void 로그인_상태_확인() throws Exception{
    //given
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    //when
    //then
    assertDoesNotThrow(() -> accountContextService.isSignIn());
  }

  @Test
  public void 작성자_확인_성공() throws Exception{
    //given
    when(accountJpaRepository.findById(anyLong())).thenReturn(
        Optional.ofNullable(Account.builder().accountId(accountId).build())
    );
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    //when
    //then
    assertDoesNotThrow(() -> accountContextService.isOwner(accountId));
  }

  @Test
  public void 작성자_확인_실패_ID_불일치() throws Exception{
    //given
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountContextService.isOwner(accountId));
    //then
    assertEquals(AccountErrorCode.NO_SUCH_ACCOUNT, exception.getErrorCode());
  }

  @Test
  public void 작성자_확인_실패_비인가_사용자() throws Exception{
    //given
    Account account = Account.builder().accountId(accountId).build();
    //when
    AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> accountContextService.isOwner(account));
    //then
    assertEquals("비정상적인 접근", exception.getMessage());
  }

  @Test
  public void 작성자_확인_실패_비인가_사용자_ID_누락() throws Exception{
    //given
    Account account = Account.builder().accountId(accountId).build();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Authentication sample = Mockito.mock(Authentication.class);
    when(securityContext.getAuthentication()).thenReturn(sample);
    SecurityContextHolder.setContext(securityContext);
    //when
    AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> accountContextService.isOwner(account));
    //then
    assertEquals("비정상적인 접근", exception.getMessage());
  }
}