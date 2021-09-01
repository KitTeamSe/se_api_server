package com.se.apiserver.v1.taglistening.application.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.common.domain.exception.NotFoundException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.taglistening.application.dto.TagListeningReadDto;
import com.se.apiserver.v1.taglistening.domain.entity.TagListening;
import com.se.apiserver.v1.taglistening.infra.repository.TagListeningJpaRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
public class TagListeningReadServiceTest {

  @Mock
  private TagListeningJpaRepository tagListeningJpaRepository;

  @Mock
  private AccountContextService accountContextService;

  @InjectMocks
  private TagListeningReadService tagListeningReadService;

  @Test
  void 단일_태그_조회_성공() {
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

    // when
    TagListeningReadDto.Response response = tagListeningReadService.read(tagListeningId);

    // then
    assertThat(response.getAccountId(), is(1L));
    assertThat(response.getAccountIdString(), is("idString"));
    assertThat(response.getTagName(), is("태그"));
  }

  @Test
  void 존재하지_않는_수신_태그() {
    // given
    Long tagListeningId = 1L;
    given(tagListeningJpaRepository.findById(tagListeningId))
        .willThrow(new NotFoundException("존재하지 않는 수신 태그"));

    // when
    NotFoundException notFoundException = assertThrows(NotFoundException.class,
        () -> tagListeningReadService.read(tagListeningId));

    // then
    assertThat(notFoundException.getMessage(), is("존재하지 않는 수신 태그"));
  }

  @Test
  void 단일_태그_조회_권한_없음() {
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
        () -> tagListeningReadService.read(tagListeningId));

    // then
    assertThat(accessDeniedException.getMessage(), is("권한 없음"));
  }

  @Test
  void 사용자_PK로_수신_태그_목록_조회_성공() {
    // given
    Long accountId = 1L;
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
    List<TagListening> tagListenings = Arrays.asList(
        new TagListening(account, tag)
        , new TagListening(account, tag)
        , new TagListening(account, tag)
    );

    given(tagListeningJpaRepository.findAllByAccountId(accountId)).willReturn(tagListenings);

    // when
    List<TagListeningReadDto.Response> responses = tagListeningReadService.readByAccountId(accountId);

    // then
    assertThat(responses.size(), is(tagListenings.size()));
  }

  @Test
  void 전체_수신_태그_조회() {
    // given
    int page = 0;
    int size = 10;
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
    Pageable pageable = new PageRequest(page, size, Direction.ASC).of();
    List<TagListening> tagListenings = new ArrayList<>();

    for (int i = 0; i < size; i++) {
      tagListenings.add(new TagListening(account, tag));
    }

    Page<TagListening> tagListeningPage = new PageImpl<>(tagListenings);
    given(tagListeningJpaRepository.findAll(pageable)).willReturn(tagListeningPage);

    // when
    Page<TagListeningReadDto.Response> responsePage = tagListeningReadService.readAllByPaging(pageable);

    // then
    assertThat(responsePage.getSize(), is(10));
  }

  @Test
  void 본인_수신_태그_조회() {
    // given
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
    List<TagListening> tagListenings = Arrays.asList(
        new TagListening(account, tag)
        , new TagListening(account, tag)
        , new TagListening(account, tag)
    );
    given(accountContextService.getContextAccount()).willReturn(account);
    given(tagListeningJpaRepository.findAllByAccountId(account.getAccountId())).willReturn(tagListenings);
    // when
    List<TagListeningReadDto.Response> responses = tagListeningReadService.readMy();

    // then
    assertThat(responses.size(), is(tagListenings.size()));
  }
}
