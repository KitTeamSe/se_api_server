package com.se.apiserver.v1.tag.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.common.domain.error.GlobalErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.tag.application.dto.TagReadDto;
import com.se.apiserver.v1.tag.application.error.TagErrorCode;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.TestExecutionListeners;

@ExtendWith(MockitoExtension.class)
public class TagReadServiceTest {
  @Mock
  private AccountContextService accountContextService;

  @Mock
  private TagJpaRepository tagJpaRepository;

  @InjectMocks
  TagReadService tagReadService;

  @Test
  void 태그_검색_성공() {
    // given
    String text = "텍스트";
    List<Tag> tags = Arrays.asList(new Tag("나는 텍스트"), new Tag("텍스트 태그"));
    given(accountContextService.isSignIn()).willReturn(true);
    given(tagJpaRepository.findAllByText(text)).willReturn(tags);

    // when
    List<TagReadDto.Response> responses = tagReadService.readMatchText(text);

    // then
    assertThat(responses.size(), is(2));
  }

  @Test
  void 전체_태그_목록_조회() {
    // given
    PageRequest pageRequest = new PageRequest(0, 10, Direction.ASC);
    List<Tag> tags = Arrays.asList(new Tag("나는 텍스트"), new Tag("텍스트 태그"));
    Page<Tag> pages = new PageImpl<>(tags);
    given(tagJpaRepository.findAll(Mockito.any(org.springframework.data.domain.PageRequest.class))).willReturn(pages);

    // when
    Page<Tag> result = tagReadService.readAll(pageRequest.of());

    // then
    assertThat(result.getTotalElements(), is(2L));
    assertThat(result.getTotalPages(), is(1));
  }

  @Test
  void 태그_검색_권한_없음() {
    // given
    String text = "텍스트";
    given(accountContextService.isSignIn()).willReturn(false);

    // when
    BusinessException businessException = assertThrows(BusinessException.class, () -> tagReadService.readMatchText(text));

    // then
    assertThat(businessException.getErrorCode(), is(GlobalErrorCode.HANDLE_ACCESS_DENIED));
    assertThat(businessException.getMessage(), is("권한 없음"));
  }

  @Test
  void 두_글자_이하의_검색어_입력() {
    // given
    String text = "잉";
    given(accountContextService.isSignIn()).willReturn(true);

    // when
    BusinessException businessException = assertThrows(BusinessException.class, () -> tagReadService.readMatchText(text));

    // then
    assertThat(businessException.getErrorCode(), is(TagErrorCode.TO_SHORT_LENGTH));
    assertThat(businessException.getMessage(), is("검색어는 최소 두 글자 이상입니다."));
  }

}