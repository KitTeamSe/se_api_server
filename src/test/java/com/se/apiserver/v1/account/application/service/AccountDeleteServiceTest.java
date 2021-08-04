package com.se.apiserver.v1.account.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class AccountDeleteServiceTest {

  @Mock
  AccountJpaRepository accountJpaRepository;
  @Mock
  AccountContextService accountContextService;

  @InjectMocks
  AccountDeleteService accountDeleteService;

  private static String idString;

  @BeforeAll
  static void setup(){
    idString = "4글자-20글자";
  }

  @Test
  public void 회원삭제_성공_회원() throws Exception{
    //given
    when(accountJpaRepository.findByIdString(idString)).thenReturn(Optional.ofNullable(Account.builder().idString(idString).build()));
    when(accountContextService.isOwner(any(Account.class))).thenReturn(true);
    //when

    //then
    assertDoesNotThrow(() -> accountDeleteService.delete(idString));
  }

  @Test
  public void 회원삭제_성공_관리자() throws Exception{
    //given
    when(accountJpaRepository.findByIdString(idString)).thenReturn(Optional.ofNullable(Account.builder().idString(idString).build()));
    when(accountContextService.hasAuthority("ACCOUNT_MANAGE")).thenReturn(true);
    //when

    //then
    assertDoesNotThrow(() -> accountDeleteService.delete(idString));
  }

  @Test
  public void 회원삭제_실패_ID_없음() throws Exception{
    //given
    //when
    BusinessException exception = assertThrows(BusinessException.class,() -> accountDeleteService.delete(idString));
    //then
    assertEquals(AccountErrorCode.NO_SUCH_ACCOUNT, exception.getErrorCode());
  }

  @Test
  public void 회원삭제_실패_권한_없음() throws Exception{
    //given
    when(accountJpaRepository.findByIdString(idString)).thenReturn(Optional.ofNullable(Account.builder().idString(idString).build()));
    //when
    AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> accountDeleteService.delete(idString));
    //then
    assertEquals("비정상적인 접근", exception.getMessage());
  }
}