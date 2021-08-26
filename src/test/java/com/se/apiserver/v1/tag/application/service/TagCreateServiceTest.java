package com.se.apiserver.v1.tag.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.common.domain.exception.PermissionDeniedException;
import com.se.apiserver.v1.common.domain.exception.UniqueValueAlreadyExistsException;
import com.se.apiserver.v1.tag.application.dto.TagCreateDto;
import com.se.apiserver.v1.tag.application.dto.TagCreateDto.Request;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TagCreateServiceTest {
  @Mock
  private TagJpaRepository tagJpaRepository;

  @Mock
  private AccountContextService accountContextService;

  @InjectMocks
  private TagCreateService tagCreateService;

  @Test
  void 태그_등록_성공(){
    // given
    TagCreateDto.Request request = new Request("태그");
    Tag tag = new Tag("태그");
    Set<String> authorities = Set.of("TAG_MANAGE");
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(tagJpaRepository.findByText(tag.getText())).willReturn(Optional.ofNullable(null));
    given(tagJpaRepository.save(any(Tag.class))).willReturn(tag);

    // when, then
    assertDoesNotThrow(() -> tagCreateService.create(request));
  }

  @Test
  void 태그_생성_권한_없음() {
    // given
    TagCreateDto.Request request = new Request("태그");
    Set<String> authorities = new HashSet<>();
    given(accountContextService.getContextAuthorities()).willReturn(authorities);

    // when
    PermissionDeniedException permissionDeniedException
        = assertThrows(PermissionDeniedException.class, () -> tagCreateService.create(request));

    // then
    assertThat(permissionDeniedException.getMessage(), is("태그 생성 권한이 없습니다"));
  }

  @Test
  void 중복된_태그명으로_인한_태그_등록_실패() {
    // given
    TagCreateDto.Request request = new Request("태그");
    Tag tag = new Tag("태그");
    Set<String> authorities = Set.of("TAG_MANAGE");
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(tagJpaRepository.findByText(tag.getText())).willReturn(Optional.of(tag));

    // when
    UniqueValueAlreadyExistsException uniqueValueAlreadyExistsException
        = assertThrows(UniqueValueAlreadyExistsException.class, () -> tagCreateService.create(request));

    // then
    assertThat(uniqueValueAlreadyExistsException.getMessage(), is("중복된 태그명입니다"));
  }

}
