package com.se.apiserver.v1.blacklist.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.blacklist.application.dto.BlacklistReadDto;
import com.se.apiserver.v1.blacklist.application.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class BlacklistReadServiceTest {

  @Mock
  private BlacklistJpaRepository blacklistJpaRepository;

  @InjectMocks
  private BlacklistReadService blacklistReadService;

  @Test
  public void 블랙리스트_읽기_성공() throws Exception{
    //given
    long id = 0L;
    Blacklist blacklist = new Blacklist("4-20글자", null, "4-20글자", null);
    when(blacklistJpaRepository.findById(anyLong())).thenReturn(Optional.of(blacklist));
    //when
    BlacklistReadDto.Response response = blacklistReadService.read(id);
    //then
    assertAll(
        () -> assertEquals(blacklist.getIp(), response.getIp()),
        () -> assertEquals(blacklist.getReason(), response.getReason())
    );
  }

  @Test
  public void 블랙리스트_읽기_실패_ID_불일치() throws Exception{
    //given
    long id = 0L;
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> blacklistReadService.read(id));
    //then
    assertEquals(BlacklistErrorCode.NO_SUCH_BLACKLIST, exception.getErrorCode());
  }

  @Test
  public void 블랙리스트_읽기_ALL_성공() throws Exception{
    //given
    PageRequest pageRequest = PageRequest.of(0, 10);
    Blacklist blacklist = new Blacklist("4-20글자", null, "4-20글자", null);
    PageImpl page = new PageImpl(Collections.singletonList(blacklist), pageRequest, 1L);
    when(blacklistJpaRepository.findAll(pageRequest)).thenReturn(page);
    //when
    PageImpl<BlacklistReadDto.Response> response = blacklistReadService.readAll(pageRequest);
    //then
    assertAll(
        () -> assertEquals(blacklist.getIp(), response.getContent().get(0).getIp()),
        () -> assertEquals(blacklist.getReason(), response.getContent().get(0).getReason()),
        () -> assertEquals(pageRequest, response.getPageable())
    );
  }
}