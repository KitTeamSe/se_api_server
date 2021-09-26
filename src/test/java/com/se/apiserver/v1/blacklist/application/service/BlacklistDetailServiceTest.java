package com.se.apiserver.v1.blacklist.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BlacklistDetailServiceTest {

  @Mock
  private BlacklistJpaRepository blacklistJpaRepository;

  @InjectMocks
  private BlacklistDetailService blacklistDetailService;

  @Test
  public void 블랙리스트_벤_여부_확인() throws Exception{
    //given
    String normalIp = "4-20글자";
    String banedIp = "127.0.0.1";
    when(blacklistJpaRepository.findByIp(anyString()))
        .thenReturn(Optional.ofNullable(null), Optional.of(mock(Blacklist.class)));
    //when
    boolean normal = blacklistDetailService.isBannedIp(normalIp);
    boolean baned = blacklistDetailService.isBannedIp(banedIp);
    //then
    assertAll(
        () -> assertEquals(false, normal),
        () -> assertEquals(true, baned)
    );
  }
}