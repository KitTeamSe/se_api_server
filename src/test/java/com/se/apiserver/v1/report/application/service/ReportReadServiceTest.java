package com.se.apiserver.v1.report.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.report.application.dto.ReportReadDto;
import com.se.apiserver.v1.report.application.error.ReportErrorCode;
import com.se.apiserver.v1.report.domain.entity.PostReport;
import com.se.apiserver.v1.report.domain.entity.Report;
import com.se.apiserver.v1.report.domain.entity.ReportStatus;
import com.se.apiserver.v1.report.infra.repository.ReportJpaRepository;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ReportReadServiceTest {

  @Mock
  private ReportJpaRepository reportJpaRepository;

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
  public void 신고_전체_조회_성공 () throws Exception{
    // given
    PageRequest pageRequest = PageRequest.of(0, 10);
    Account reporter = mock(Account.class);
    Post post = mock(Post.class);
    Report report = new PostReport(post, "신고내용 5자-255자", reporter);
    PageImpl page = new PageImpl(Collections.singletonList(report), pageRequest, 1L);
    when(reportJpaRepository.findAll(pageRequest)).thenReturn(page);
    // when
    PageImpl<ReportReadDto.Response> response = reportReadService.readAll(pageRequest);
    // then
    assertAll(
        () -> assertEquals(report.getReportId(), response.getContent().get(0).getReportId()),
        () -> assertEquals(report.getDescription(), response.getContent().get(0).getDescription()),
        () -> assertEquals(pageRequest, response.getPageable())
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
}