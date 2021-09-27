package com.se.apiserver.v1.account.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountVerifyStatus;
import com.se.apiserver.v1.account.domain.entity.AccountVerifyToken;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.AccountVerifyTokenJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.mail.application.service.MailSendService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class AccountVerifyServiceTest {

  @Mock
  private AccountJpaRepository accountJpaRepository;
  @Mock
  private AccountVerifyTokenJpaRepository accountVerifyTokenJpaRepository;
  @Mock
  private JavaMailSender javaMailSender;
  @Mock
  private MailSendService mailSendService;

  @InjectMocks
  private AccountVerifyService accountVerifyService;

  private static String token;

  @BeforeAll
  static void setup(){
    token = "t".repeat(255);
  }

  @Test
  public void 인증_발송_성공_이메일() throws Exception{
    //given
    //when
    //then
    assertDoesNotThrow(() -> accountVerifyService.sendVerifyRequestEmail("test@test.com"));
  }

  @Test
  public void 인증_발송_성공_계정() throws Exception{
    //given
    String id = "4글자-20글자";
    String email = "test@test.com";
    Account sample = Account.builder().idString(id).email(email).build();
    when(accountJpaRepository.findByIdString(anyString())).thenReturn(Optional.ofNullable(sample));
    //when
    //then
    assertDoesNotThrow(() -> accountVerifyService.sendVerifyRequestEmailByAccountId(id));
  }

  @Test
  public void 인증_발송_실패_계정_ID_불일치() throws Exception{
    //given
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountVerifyService.sendVerifyRequestEmailByAccountId(anyString()));
    //then
    assertEquals(AccountErrorCode.NO_SUCH_ACCOUNT, exception.getErrorCode());
  }

  @Test
  public void 인증_성공() throws Exception{
    //given
    when(accountVerifyTokenJpaRepository.findFirstByToken(anyString())).thenReturn(
        Optional.of(
            new AccountVerifyToken(null, null,
                LocalDateTime.now().plusHours(1),
                AccountVerifyStatus.UNVERIFIED))
    );
    //when
    //then
    assertDoesNotThrow(() -> accountVerifyService.verify(token));
  }

  @Test
  public void 인증_실패_토큰_불일치() throws Exception{
    //given

    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountVerifyService.verify(token));
    //then
    assertEquals(AccountErrorCode.NO_SUCH_TOKEN, exception.getErrorCode());
  }
  
  @Test
  public void 인증_실패_인증된_사용자() throws Exception{
    //given
    when(accountVerifyTokenJpaRepository.findFirstByToken(anyString())).thenReturn(
        Optional.of(
            new AccountVerifyToken(null, null,
                LocalDateTime.now().plusHours(1),
                AccountVerifyStatus.VERIFIED))
    );
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountVerifyService.verify(token));
    //then
    assertEquals(AccountErrorCode.ALREADY_VERIFIED, exception.getErrorCode());
  }
  
  @Test
  public void 인증_실패_토큰_만료() throws Exception{
    //given
    when(accountVerifyTokenJpaRepository.findFirstByToken(anyString())).thenReturn(
        Optional.of(
            new AccountVerifyToken(null, null,
                LocalDateTime.now().minusHours(1),
                AccountVerifyStatus.UNVERIFIED))
    );
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> accountVerifyService.verify(token));
    //then
    assertEquals(AccountErrorCode.EMAIL_VERIFY_TOKEN_EXPIRED, exception.getErrorCode());
  }
}