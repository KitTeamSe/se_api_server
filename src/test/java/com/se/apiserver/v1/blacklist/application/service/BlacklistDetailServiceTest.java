package com.se.apiserver.v1.blacklist.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.tomcat.jni.Local;
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
  public void 블랙리스트_IP_차단_여부_확인() throws Exception{
    //given
    String normalIp = "4-20글자";
    String bannedIp = "127.0.0.1";
    when(blacklistJpaRepository.findByIpAndReleaseDateAfter(anyString(), any(LocalDateTime.class)))
        .thenReturn(Collections.emptyList(), Collections.singletonList(mock(Blacklist.class)));
    //when
    boolean normal = blacklistDetailService.isBannedIp(normalIp);
    boolean banned = blacklistDetailService.isBannedIp(bannedIp);
    //then
    assertAll(
        () -> assertEquals(false, normal),
        () -> assertEquals(true, banned)
    );
  }
  
  @Test
  public void 블랙리스트_계정_차단_여부_확인() throws Exception{
    // given
    Account normalAccount = mock(Account.class);
    Account bannedAccount = mock(Account.class);
    when(blacklistJpaRepository.findByAccountAndReleaseDateAfter(any(Account.class), any(LocalDateTime.class)))
        .thenReturn(Collections.emptyList(), Collections.singletonList(mock(Blacklist.class)));
    // when
    boolean normal = blacklistDetailService.isBannedAccount(normalAccount);
    boolean banned = blacklistDetailService.isBannedAccount(bannedAccount);
    // then
    assertAll(
        () -> assertEquals(false, normal),
        () -> assertEquals(true, banned)
    );
  }
}