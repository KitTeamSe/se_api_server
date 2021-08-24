package com.se.apiserver.v1.report.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import com.se.apiserver.v1.report.application.dto.ReportCreateDto;
import com.se.apiserver.v1.report.application.dto.ReportCreateDto.Request;
import com.se.apiserver.v1.report.application.error.ReportErrorCode;
import com.se.apiserver.v1.report.domain.entity.ReportType;
import com.se.apiserver.v1.report.infra.repository.ReportJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportCreateServiceTest {

  @Mock
  private AccountContextService accountContextService;
  @Mock
  private ReportJpaRepository reportJpaRepository;
  @Mock
  private PostJpaRepository postJpaRepository;
  @Mock
  private ReplyJpaRepository replyJpaRepository;

  @InjectMocks
  private ReportCreateService reportCreateService;

  @Test
  public void 게시글_신고_성공 () throws Exception{
    // given
    ReportCreateDto.Request request = new Request(ReportType.POST, 1L, "신고내용 - 5자이상 255자 이하");
    when(accountContextService.isSignIn()).thenReturn(true);
    Post post = mock(Post.class);
    when(postJpaRepository.findById(request.getTargetId())).thenReturn(Optional.ofNullable(post));
    // when
    // then
    assertDoesNotThrow(() -> reportCreateService.create(request));
  }

  @Test
  public void 댓글_신고_성공() throws Exception{
    // given
    ReportCreateDto.Request request = new Request(ReportType.REPLY, 1L, "신고내용 - 5자이상 255자 이하");
    when(accountContextService.isSignIn()).thenReturn(true);
    Account reporter = mock(Account.class);
    when(accountContextService.getContextAccount()).thenReturn(reporter);
    Reply reply = mock(Reply.class);
    when(replyJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(reply));
    // when
    // then
    assertDoesNotThrow(() -> reportCreateService.create(request));
  }
  
  
  @Test
  public void 유효하지_않은_신고타입 () throws Exception{
    // given
    ReportCreateDto.Request request = mock(Request.class);
    // when
    BusinessException exception = assertThrows(BusinessException.class, ()-> reportCreateService.create(request));
    // then
    assertEquals(ReportErrorCode.INVALID_INPUT, exception.getErrorCode());
  }
  
  @Test
  public void 익명_사용자_신고_불가 () throws Exception{
    // given
    ReportCreateDto.Request request = mock(Request.class);
    when(request.getReportType()).thenReturn(ReportType.POST);
    // when
    BusinessException exception = assertThrows(BusinessException.class, ()-> reportCreateService.create(request));
    // then
    assertEquals(ReportErrorCode.ANONYMOUS_CANT_REPORT, exception.getErrorCode());
  }

  
  @Test
  public void 존재하지_않는_게시물 () throws Exception{
    // given
    ReportCreateDto.Request request = mock(Request.class);
    when(request.getReportType()).thenReturn(ReportType.POST);
    when(accountContextService.isSignIn()).thenReturn(true);
    // when
    BusinessException exception = assertThrows(BusinessException.class, ()-> reportCreateService.create(request));
    // then
    assertEquals(PostErrorCode.NO_SUCH_POST, exception.getErrorCode());
  }
  
  @Test
  public void 존재하지_않는_댓글() throws Exception{
    // given
    ReportCreateDto.Request request = mock(Request.class);
    when(request.getReportType()).thenReturn(ReportType.REPLY);
    when(accountContextService.isSignIn()).thenReturn(true);
    // when
    BusinessException exception = assertThrows(BusinessException.class, ()-> reportCreateService.create(request));
    // then
    assertEquals(ReplyErrorCode.NO_SUCH_REPLY, exception.getErrorCode());
  }
  
  @Test
  public void 자기_게시글_신고_불가능 () throws Exception{
    // given
    ReportCreateDto.Request request = mock(Request.class);
    when(request.getReportType()).thenReturn(ReportType.POST);
    when(accountContextService.isSignIn()).thenReturn(true);
    Account account = mock(Account.class);
    Post post = mock(Post.class);
    when(accountContextService.getContextAccount()).thenReturn(account);
    when(postJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(post));
    when(post.isOwner(account)).thenReturn(true);
    // when
    BusinessException exception = assertThrows(BusinessException.class, ()-> reportCreateService.create(request));
    // then
    assertEquals(ReportErrorCode.CANNOT_REPORT_MYSELF, exception.getErrorCode());
  }

  @Test
  public void 자기_댓글_신고_불가능 () throws Exception{
    // given
    ReportCreateDto.Request request = mock(Request.class);
    when(request.getReportType()).thenReturn(ReportType.REPLY);
    when(accountContextService.isSignIn()).thenReturn(true);
    Account account = mock(Account.class);
    Reply reply = mock(Reply.class);
    when(accountContextService.getContextAccount()).thenReturn(account);
    when(replyJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(reply));
    when(reply.isOwner(anyLong())).thenReturn(true);
    // when
    BusinessException exception = assertThrows(BusinessException.class, ()-> reportCreateService.create(request));
    // then
    assertEquals(ReportErrorCode.CANNOT_REPORT_MYSELF, exception.getErrorCode());
  }

}