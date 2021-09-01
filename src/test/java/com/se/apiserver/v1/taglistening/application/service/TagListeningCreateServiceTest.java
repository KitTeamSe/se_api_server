package com.se.apiserver.v1.taglistening.application.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.common.domain.exception.NotFoundException;
import com.se.apiserver.v1.common.domain.exception.PreconditionFailedException;
import com.se.apiserver.v1.common.domain.exception.UnauthenticatedException;
import com.se.apiserver.v1.common.domain.exception.UniqueValueAlreadyExistsException;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import com.se.apiserver.v1.taglistening.application.dto.TagListeningCreateDto;
import com.se.apiserver.v1.taglistening.application.dto.TagListeningCreateDto.Request;
import com.se.apiserver.v1.taglistening.domain.entity.TagListening;
import com.se.apiserver.v1.taglistening.infra.repository.TagListeningJpaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TagListeningCreateServiceTest {

  @Mock
  private TagListeningJpaRepository tagListeningJpaRepository;

  @Mock
  private TagJpaRepository tagJpaRepository;

  @Mock
  private AccountContextService accountContextService;

  @InjectMocks
  private TagListeningCreateService tagListeningCreateService;

  @Test
  void 수신_태그_등록_성공() {
    // given
    TagListeningCreateDto.Request request = Request
        .builder()
        .tagId(1L)
        .build();
    Tag tag = new Tag("태그");
    Account account = new Account(1L
        , "idString"
        , "password"
        , "name"
        , "nickName"
        , "studentId"
        , AccountType.ASSISTANT
        , "phoneNumber"
        , "email@kumoh.ac.kr"
        , "lastSignInIp"
        , InformationOpenAgree.AGREE
        , null
        , null);

    given(tagJpaRepository.findById(request.getTagId())).willReturn(java.util.Optional.of(tag));
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getContextAccount()).willReturn(account);
    given(tagListeningJpaRepository.findByAccountIdAndTagId(account.getAccountId(), tag.getTagId()))
        .willReturn(Optional.ofNullable(null));
    given(tagListeningJpaRepository.save(Mockito.any(TagListening.class)))
        .willReturn(Mockito.any(TagListening.class));

    // when, then
    assertDoesNotThrow(() -> tagListeningCreateService.create(request));
  }

  @Test
  void 존재하지_않는_태그() {
    // given
    TagListeningCreateDto.Request request = Request
        .builder()
        .tagId(1L)
        .build();

    given(tagJpaRepository.findById(request.getTagId()))
        .willThrow(new NotFoundException("존재하지 않는 태그"));

    // when
    NotFoundException notFoundException = assertThrows(NotFoundException.class,
        () -> tagListeningCreateService.create(request));

    // then
    assertThat(notFoundException.getMessage(), is("존재하지 않는 태그"));
  }

  @Test
  void 로그인하지_않은_사용자가_수신_태그_등록_실패() {
    // given
    TagListeningCreateDto.Request request = Request
        .builder()
        .tagId(1L)
        .build();
    Tag tag = new Tag("태그");

    given(tagJpaRepository.findById(request.getTagId())).willReturn(java.util.Optional.of(tag));
    given(accountContextService.isSignIn()).willReturn(false);

    // when
    UnauthenticatedException unauthenticatedException
        = assertThrows(UnauthenticatedException.class
        , () -> tagListeningCreateService.create(request));

    // then
    assertThat(unauthenticatedException.getMessage(), is("수신 태그는 로그인 후 등록 가능합니다"));
  }

  @Test
  void 중복_수신_태그_등록_요청_시_등록_실패() {
    // given
    TagListeningCreateDto.Request request = Request
        .builder()
        .tagId(1L)
        .build();
    Tag tag = new Tag("태그");
    Account account = new Account(1L
        , "idString"
        , "password"
        , "name"
        , "nickName"
        , "studentId"
        , AccountType.ASSISTANT
        , "phoneNumber"
        , "email@kumoh.ac.kr"
        , "lastSignInIp"
        , InformationOpenAgree.AGREE
        , null
        , null);
    given(tagJpaRepository.findById(request.getTagId())).willReturn(java.util.Optional.of(tag));
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getContextAccount()).willReturn(account);
    given(tagListeningJpaRepository.findByAccountIdAndTagId(account.getAccountId(), tag.getTagId()))
        .willReturn(Optional.of(new TagListening(account, tag)));

    // when
    UniqueValueAlreadyExistsException uniqueValueAlreadyExistsException
        = assertThrows(UniqueValueAlreadyExistsException.class, () -> tagListeningCreateService.create(request));

    // then
    assertThat(uniqueValueAlreadyExistsException.getMessage(), is("중복된 내용 삽입"));
  }

  @Test
  void 등록_가능한_최대_수신_태그_개수_초과() {
    // given
    TagListeningCreateDto.Request request = Request
        .builder()
        .tagId(1L)
        .build();
    Tag tag = new Tag("태그");
    Account account = new Account(1L
        , "idString"
        , "password"
        , "name"
        , "nickName"
        , "studentId"
        , AccountType.ASSISTANT
        , "phoneNumber"
        , "email@kumoh.ac.kr"
        , "lastSignInIp"
        , InformationOpenAgree.AGREE
        , null
        , null);

    List<TagListening> tagListenings = new ArrayList<>();

    for (int i = 0; i < TagListening.MAX_TAG_LISTENING_CAPACITY; i++) {
      tagListenings.add(new TagListening(account, tag));
    }

    given(tagJpaRepository.findById(request.getTagId())).willReturn(java.util.Optional.of(tag));
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getContextAccount()).willReturn(account);
    given(tagListeningJpaRepository.findAllByAccountId(account.getAccountId())).willReturn(tagListenings);

    // when
    PreconditionFailedException preconditionFailedException
        = assertThrows(PreconditionFailedException.class, () -> tagListeningCreateService.create(request));

    // then
    assertThat(preconditionFailedException.getMessage(), is("등록 가능한 최대 수신 태그 개수를 초과하였습니다"));
  }
}
