package com.se.apiserver.v1.report.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.report.application.error.ReportErrorCode;
import com.se.apiserver.v1.report.domain.entity.Report;
import com.se.apiserver.v1.report.infra.repository.ReportJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportDeleteServiceTest {

  @Mock
  private ReportJpaRepository reportJpaRepository;

  @InjectMocks
  private ReportDeleteService reportDeleteService;

  @Test
  public void 신고_삭제_성공 () throws Exception{
    // given
    Report report = mock(Report.class);
    when(reportJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(report));
    // when
    // then
    assertDoesNotThrow(() -> reportDeleteService.delete(1L));
  }

  @Test
  public void 존재하지_않는_신고 () throws Exception{
    // given
    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> reportDeleteService.delete(0L));
    // then
    assertEquals(ReportErrorCode.NO_SUCH_REPORT, exception.getErrorCode());
  }
}