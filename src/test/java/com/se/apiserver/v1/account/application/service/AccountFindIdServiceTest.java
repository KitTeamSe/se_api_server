package com.se.apiserver.v1.account.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.dto.AccountFindIdByEmailDto;
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

@ExtendWith(MockitoExtension.class)
class AccountFindIdServiceTest {
  @Mock
  private AccountJpaRepository accountJpaRepository;

  @InjectMocks
  private AccountFindIdService accountFindIdService;

  private static String idString;
  private static String email;

  @BeforeAll
  static void setup(){
     idString = "4글자-20글자";
     email = "test@test.com";
  }

  @Test
  public void 회원검색_성공() throws Exception{
    //given
    when(accountJpaRepository.findByEmail(email)).thenReturn(Optional.ofNullable(Account.builder().idString(idString).build()));
    //when
    AccountFindIdByEmailDto.Response response = accountFindIdService.readByEmail(email);
    //then
    assertEquals(idString.substring(0, idString.length() - 2) + "**", response.getId());
  }

  @Test
  public void 회원검색_실패_이메일_없음() throws Exception{
    //given
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountFindIdService.readByEmail(email));
    //then
    assertEquals(AccountErrorCode.NO_SUCH_ACCOUNT, exception.getErrorCode());
  }
}