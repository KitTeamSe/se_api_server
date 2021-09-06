package com.se.apiserver.v1.attach.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.service.MultipartFileDeleteService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AttachDeleteServiceTest {

  @Mock
  private MultipartFileDeleteService multipartFileDeleteService;
  @Mock
  private AttachJpaRepository attachJpaRepository;
  @InjectMocks
  AttachDeleteService attachDeleteService;

  @Test
  void 단일_첨부_파일_삭제_성공() {
    // given
    Long attachcId = 1L;
    Attach attach = new Attach("URL", "fileName", 1L);
    given(attachJpaRepository.findById(attachcId)).willReturn(java.util.Optional.of(attach));
    willDoNothing().given(multipartFileDeleteService).delete(attach.getSaveName());
    willDoNothing().given(attachJpaRepository).delete(attach);

    // when
    boolean result = attachDeleteService.delete(attachcId);

    // then
    assertThat(result, is(true));
  }

  @Test
  void 존재하지_않는_첨부_파일() {
    // given
    Long attachId = 1L;
    given(attachJpaRepository.findById(attachId))
        .willThrow(new BusinessException(AttachErrorCode.NO_SUCH_ATTACH));

    // when
    BusinessException businessException = assertThrows(BusinessException.class,
        () -> attachDeleteService.delete(attachId));

    // then
    assertThat(businessException.getErrorCode(), is(AttachErrorCode.NO_SUCH_ATTACH));
    assertThat(businessException.getMessage(), is("존재하지 않는 첨부파일"));
  }

  @Test
  void 게시글_첨부_파일_삭제_성공() {
    // given
    Long postId = 1L;
    List<Attach> attaches
        = Arrays.asList(new Attach("URL1", "fileName1", 1L)
        , new Attach("URL2", "fileName2", 1L));

    given(attachJpaRepository.findAllByPostId(postId)).willReturn(attaches);
    willDoNothing().given(attachJpaRepository).deleteAttachesByPostId(postId);

    // when
    boolean result = attachDeleteService.deleteAllByOwnerId(postId, null);

    // then
    assertThat(result, is(true));
  }

  @Test
  void 댓글_첨부_파일_삭제_성공() {
    // given
    Long replyId = 1L;
    List<Attach> attaches
        = Arrays.asList(new Attach("URL1", "fileName1", 1L)
        , new Attach("URL2", "fileName2", 1L));

    given(attachJpaRepository.findAllByReplyId(replyId)).willReturn(attaches);
    willDoNothing().given(attachJpaRepository).deleteAttachesByReplyId(replyId);

    // when
    boolean result = attachDeleteService.deleteAllByOwnerId(null, replyId);

    // then
    assertThat(result, is(true));
  }

  @Test
  void 올바르지_않은_입력_값() {
    // given
    Long postId = 1L;
    Long replyId = 1L;

    // when
    BusinessException businessException = assertThrows(BusinessException.class
        , () -> attachDeleteService.deleteAllByOwnerId(postId, replyId));

    // then
    assertThat(businessException.getErrorCode(), is(AttachErrorCode.INVALID_INPUT));
    assertThat(businessException.getMessage(), is("입력 값이 올바르지 않음"));
  }
}
