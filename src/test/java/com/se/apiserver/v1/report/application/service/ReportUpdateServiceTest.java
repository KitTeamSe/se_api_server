package com.se.apiserver.v1.report.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.report.application.dto.ReportUpdateDto;
import com.se.apiserver.v1.report.application.dto.ReportUpdateDto.Request;
import com.se.apiserver.v1.report.application.error.ReportErrorCode;
import com.se.apiserver.v1.report.domain.entity.Report;
import com.se.apiserver.v1.report.domain.entity.ReportResult;
import com.se.apiserver.v1.report.domain.entity.ReportStatus;
import com.se.apiserver.v1.report.infra.repository.ReportJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportUpdateServiceTest {

  @Mock
  private AccountContextService accountContextService;
  @Mock
  private ReportJpaRepository reportJpaRepository;

  @InjectMocks
  private ReportUpdateService reportUpdateService;

  @Test
  public void 신고_상태가_COMPLETED_성공 () throws Exception{
    // given
    ReportUpdateDto.Request request = new Request(1L, ReportResult.TARGET_DELETE);
    Report report = mock(Report.class);
    when(reportJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(report));
    Account processor = mock(Account.class);
    when(accountContextService.getContextAccount()).thenReturn(processor);
    when(report.getReportStatus()).thenReturn(ReportStatus.COMPLETED);
    // when
    // then
    assertDoesNotThrow(() -> reportUpdateService.update(request));
  }

  @Test
  public void 신고_상태가_COMPLETED_아닌경우_성공() throws Exception{
    // given
    ReportUpdateDto.Request request = new Request(1L, ReportResult.TARGET_DELETE);
    Report report = mock(Report.class);
    when(reportJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(report));
    Account processor = mock(Account.class);
    when(accountContextService.getContextAccount()).thenReturn(processor);
    when(report.getReportStatus()).thenReturn(ReportStatus.REVIEWING);
    // when
    // then
    assertDoesNotThrow(() -> reportUpdateService.update(request));
  }


  @Test
  public void 존재하지_않는_신고 () throws Exception{
    // given
    ReportUpdateDto.Request request = mock(ReportUpdateDto.Request.class);
    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> reportUpdateService.update(request));
    // then
    assertEquals(ReportErrorCode.NO_SUCH_REPORT, exception.getErrorCode());
  }

}