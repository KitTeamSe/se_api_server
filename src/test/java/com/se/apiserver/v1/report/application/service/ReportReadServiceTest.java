package com.se.apiserver.v1.report.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.report.application.dto.ReportReadDto;
import com.se.apiserver.v1.report.application.dto.ReportReadDto.ReportSearchRequest;
import com.se.apiserver.v1.report.application.error.ReportErrorCode;
import com.se.apiserver.v1.report.domain.entity.PostReport;
import com.se.apiserver.v1.report.domain.entity.Report;
import com.se.apiserver.v1.report.domain.entity.ReportStatus;
import com.se.apiserver.v1.report.infra.repository.ReportJpaRepository;
import com.se.apiserver.v1.report.infra.repository.ReportQueryRepository;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class ReportReadServiceTest {

  @Mock
  private ReportJpaRepository reportJpaRepository;
  @Mock
  private ReportQueryRepository reportQueryRepository;
  @Mock
  private AccountContextService accountContextService;

  @InjectMocks
  private ReportReadService reportReadService;


  @Test
  public void 신고_단일_조회_상태변경없음_성공 () throws Exception{
    // given
    Account reporter = mock(Account.class);
    Post post = mock(Post.class);
    Report report = new PostReport(post, "신고내용 5자-255자", reporter);
    when(reportJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(report));
    // when
    ReportReadDto.Response response = reportReadService.read(1L);
    // then
    assertAll(
        () -> assertEquals(report.getReportId(), response.getReportId()),
        () -> assertEquals(report.getDescription(), response.getDescription()),
        () -> assertEquals(report.getReportStatus(), response.getReportStatus()),
        () -> assertEquals(report.getReportResult(), response.getReportResult()),
        () -> assertEquals(report.getReporter().getAccountId(), response.getReporterId()),
        () -> assertEquals(report.getProcessor() == null ? null : response.getProcessorId(), response.getProcessorId())
    );
  }

  @Test
  public void 신고_단일_조회_상태변경_성공 () throws Exception{
    // given
    Account reporter = mock(Account.class);
    Post post = mock(Post.class);
    Report report = new PostReport(post, "신고내용 5자-255자", reporter);
    when(reportJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(report));
    // when
    ReportReadDto.Response response = reportReadService.read(1L);
    // then
    assertAll(
        () -> assertEquals(report.getReportId(), response.getReportId()),
        () -> assertEquals(ReportStatus.REVIEWING, response.getReportStatus())
    );
  }

  @Test
  public void 신고_검색_및_전체_조회_성공 () throws Exception{
    // given
    PageRequest pageRequest = new PageRequest(0, 10, Direction.ASC);
    ReportSearchRequest request = new ReportSearchRequest(null, null, null, null, null, pageRequest);

    Account reporter = mock(Account.class);
    Post post = mock(Post.class);
    Report report = new PostReport(post, "신고내용 5자-255자", reporter);

    long totalElement = 1L;
    PageImpl page = new PageImpl(Collections.singletonList(report), pageRequest.of(), totalElement);
    when(reportQueryRepository.search(any(ReportSearchRequest.class))).thenReturn(page);
    // when
    PageImpl<ReportReadDto.Response> result = reportReadService.readAll(request);
    // then
    assertAll(
        () -> assertEquals(report.getDescription(), result.getContent().get(0).getDescription()),
        () -> assertEquals(pageRequest.of(), result.getPageable()),
        () -> assertEquals(totalElement, result.getTotalElements())
    );
  }

  @Test
  public void 내가_작성한_신고_조회_성공 () throws Exception{
    // given
    PageRequest pageRequest = new PageRequest(0, 10, Direction.ASC);
    Account owner = mock(Account.class);
    when(accountContextService.isSignIn()).thenReturn(true);
    when(accountContextService.getContextAccount()).thenReturn(owner);

    Post post = mock(Post.class);
    Report report = new PostReport(post, "신고내용 5자-255자", owner);
    long totalElement = 1L;
    PageImpl page = new PageImpl(Collections.singletonList(report), pageRequest.of(), totalElement);
    when(reportJpaRepository.findAllByReporter(any(Pageable.class), any(Account.class))).thenReturn(page);
    // when
    PageImpl<ReportReadDto.Response> result = reportReadService.readOwn(pageRequest.of());
    // then
    assertAll(
        () -> assertEquals(report.getDescription(), result.getContent().get(0).getDescription()),
        () -> assertEquals(pageRequest.of(), result.getPageable()),
        () -> assertEquals(totalElement, result.getTotalElements())
    );
  }


  @Test
  public void 존재하지_않는_신고 () throws Exception{
    // given
    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> reportReadService.read(0L));
    // then
    assertEquals(ReportErrorCode.NO_SUCH_REPORT, exception.getErrorCode());
  }

  @Test
  public void 로그인되지_않은_사용자 () throws Exception{
    // given
    PageRequest request = new PageRequest(0, 10, Direction.ASC);
    // when
    AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> reportReadService.readOwn(request.of()));
    // then
    assertEquals("로그인 후 접근가능", exception.getMessage());
  }
}