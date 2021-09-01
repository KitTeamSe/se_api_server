package com.se.apiserver.v1.blacklist.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.blacklist.application.dto.BlacklistCreateDto;
import com.se.apiserver.v1.blacklist.application.dto.BlacklistCreateDto.Request;
import com.se.apiserver.v1.blacklist.application.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
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

  @InjectMocks
  private BlacklistCreateService blacklistCreateService;

  @Test
  public void 블랙리스트_생성_성공() throws Exception{
    //given
    BlacklistCreateDto.Request request = new Request("127.0.0.1", "4-20글자");
    //when
    //then
    assertDoesNotThrow(() -> blacklistCreateService.create(request));
  }

  @Test
  public void 블랙리스트_생성_실패_IP_중복() throws Exception{
    //given
    BlacklistCreateDto.Request request = new Request("127.0.0.1", "4-20글자");
    when(blacklistJpaRepository.findByIp(anyString())).thenReturn(Optional.of(mock(Blacklist.class)));
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> blacklistCreateService.create(request));
    //then
    assertEquals(BlacklistErrorCode.DUPLICATED_BLACKLIST, exception.getErrorCode());
  }
}