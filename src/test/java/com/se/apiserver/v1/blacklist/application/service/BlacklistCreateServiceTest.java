package com.se.apiserver.v1.blacklist.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.blacklist.application.dto.BlacklistCreateDto;
import com.se.apiserver.v1.blacklist.application.dto.BlacklistCreateDto.Request;
import com.se.apiserver.v1.blacklist.application.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BlacklistCreateServiceTest {

  @Mock
  private BlacklistJpaRepository blacklistJpaRepository;
  @Mock
  private AccountJpaRepository accountJpaRepository;

  @InjectMocks
  private BlacklistCreateService blacklistCreateService;

  @Test
  public void IP_블랙리스트_생성_성공() throws Exception{
    //given
    BlacklistCreateDto.Request request = new Request("127.0.0.1",null,"4-20글자", LocalDateTime.now());
    //when
    //then
    assertDoesNotThrow(() -> blacklistCreateService.create(request));
  }

  @Test
  public void 계정_블랙리스트_생성_성공() throws Exception{
    // given
    BlacklistCreateDto.Request request = new Request(null,1L,"4-20글자", LocalDateTime.now());
    when(accountJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(mock(Account.class)));
    // when
    // then
    assertDoesNotThrow(() -> blacklistCreateService.create(request));
  }

  @Test
  public void 블랙리스트_설정대상_없음() throws Exception{
    // given
    BlacklistCreateDto.Request request = new Request(null,null,"4-20글자", LocalDateTime.now());
    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> blacklistCreateService.create(request));
    // then
    assertEquals(BlacklistErrorCode.REQUIRED_AT_LEAST, exception.getErrorCode());
  }

}