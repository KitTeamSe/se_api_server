package com.se.apiserver.v1.tag.application.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.tag.application.error.TagErrorCode;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import com.se.apiserver.v1.taglistening.infra.repository.TagListeningJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TagDeleteServiceTest {

  @Mock
  private TagJpaRepository tagJpaRepository;

  @Mock
  private TagListeningJpaRepository tagListeningJpaRepository;

  @InjectMocks
  private TagDeleteService tagDeleteService;

  @Test
  void 태그_삭제_성공() {
    // given
    Long tagId = 1L;
    Tag tag = new Tag("태그");
    given(tagJpaRepository.findById(tagId)).willReturn(java.util.Optional.of(tag));
    willDoNothing().given(tagListeningJpaRepository).deleteAllByTag(tag);
    willDoNothing().given(tagJpaRepository).delete(tag);

    // when
    boolean result = tagDeleteService.delete(tagId);

    // then
    assertThat(result, is(true));
  }

  @Test
  void 존재하지_않는_태그() {
    // given
    Long tagId = 1L;
    given(tagJpaRepository.findById(tagId)).willThrow(new BusinessException(TagErrorCode.NO_SUCH_TAG));

    // when
    BusinessException businessException = assertThrows(BusinessException.class, () -> tagDeleteService.delete(tagId));

    // then
    assertThat(businessException.getErrorCode(), is(TagErrorCode.NO_SUCH_TAG));
    assertThat(businessException.getMessage(), is("존재하지 않는 태그"));
  }
}
