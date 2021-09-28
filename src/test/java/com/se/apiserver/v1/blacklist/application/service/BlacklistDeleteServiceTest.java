package com.se.apiserver.v1.blacklist.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.blacklist.application.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BlacklistDeleteServiceTest {

  @Mock
  private BlacklistJpaRepository blacklistJpaRepository;

  @InjectMocks
  private BlacklistDeleteService blacklistDeleteService;

  @Test
  public void 블랙리스트_삭제_성공() throws Exception{
    //given
    long id = 0L;
    when(blacklistJpaRepository.findById(anyLong())).thenReturn(Optional.of(mock(Blacklist.class)));
    //when
    //then
    assertDoesNotThrow(() -> blacklistDeleteService.delete(id));
  }

  @Test
  public void 블랙리스트_삭제_실패_ID_불일치() throws Exception{
    //given
    long id = 0L;
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> blacklistDeleteService.delete(id));
    //then
    assertEquals(BlacklistErrorCode.NO_SUCH_BLACKLIST, exception.getErrorCode());
  }
}