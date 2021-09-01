package com.se.apiserver.v1.taglistening.application.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.common.domain.exception.NotFoundException;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.taglistening.domain.entity.TagListening;
import com.se.apiserver.v1.taglistening.infra.repository.TagListeningJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
public class TagListeningDeleteServiceTest {

  @Mock
  private TagListeningJpaRepository tagListeningJpaRepository;

  @Mock
  private AccountContextService accountContextService;

  @InjectMocks
  private TagListeningDeleteService tagListeningDeleteService;

  @Test
  void 수신_태그_삭제_성공() {
    // given
    Long tagListeningId = 1L;
    Account account = new Account(1L
        , "idString"
        , "password"
        , "name"
        , "nickname"
        , "studentId"
        , AccountType.ASSISTANT
        , "phoneNumber"
        , "email@kumoh.ac.kr"
        , "lastSingInIp"
        , InformationOpenAgree.AGREE
        , null
        , null);
    Tag tag = new Tag("태그");
    TagListening tagListening = new TagListening(account, tag);

    given(tagListeningJpaRepository.findById(tagListeningId)).willReturn(
        java.util.Optional.of(tagListening));
    given(accountContextService.isOwner(tagListening.getAccount())).willReturn(true);
    willDoNothing().given(tagListeningJpaRepository).delete(tagListening);

    // when
    boolean result = tagListeningDeleteService.delete(tagListeningId);

    // then
    assertThat(result, is(true));
  }

  @Test
  void 존재하지_않는_수신_태그() {
    // given
    Long tagListeningId = 1L;
    given(tagListeningJpaRepository.findById(tagListeningId))
        .willThrow(new NotFoundException("존재하지 않는 수신 태그"));
    // when
    NotFoundException notFoundException = assertThrows(NotFoundException.class,
        () -> tagListeningDeleteService.delete(tagListeningId));

    // then
    assertThat(notFoundException.getMessage(), is("존재하지 않는 수신 태그"));
  }

  @Test
  void 수신_태그_삭제_권한_없음() {
    // given
    Long tagListeningId = 1L;
    Account account = new Account(1L
        , "idString"
        , "password"
        , "name"
        , "nickname"
        , "studentId"
        , AccountType.ASSISTANT
        , "phoneNumber"
        , "email@kumoh.ac.kr"
        , "lastSingInIp"
        , InformationOpenAgree.AGREE
        , null
        , null);
    Tag tag = new Tag("태그");
    TagListening tagListening = new TagListening(account, tag);

    given(tagListeningJpaRepository.findById(tagListeningId)).willReturn(
        java.util.Optional.of(tagListening));
    given(accountContextService.isOwner(tagListening.getAccount())).willReturn(false);
    given(accountContextService.hasAuthority("TAG_MANAGE")).willReturn(false);

    // when
    AccessDeniedException accessDeniedException = assertThrows(AccessDeniedException.class,
        () -> tagListeningDeleteService.delete(tagListeningId));

    // then
    assertThat(accessDeniedException.getMessage(), is("권한 없음"));
  }
}
