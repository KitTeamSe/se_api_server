package com.se.apiserver.v1.report.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.report.application.dto.ReportUpdateDto;
import com.se.apiserver.v1.report.application.error.ReportErrorCode;
import com.se.apiserver.v1.report.domain.entity.Report;
import com.se.apiserver.v1.report.domain.entity.ReportResult;
import com.se.apiserver.v1.report.domain.entity.ReportStatus;
import com.se.apiserver.v1.report.infra.repository.ReportJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReportUpdateService {

  private final AccountContextService accountContextService;
  private final ReportJpaRepository reportJpaRepository;

  public ReportUpdateService(
      AccountContextService accountContextService,
      ReportJpaRepository reportJpaRepository) {
    this.accountContextService = accountContextService;
    this.reportJpaRepository = reportJpaRepository;
  }

  @Transactional
  public Long update(ReportUpdateDto.Request request) {

    Report report = reportJpaRepository.findById(request.getReportId())
        .orElseThrow(() -> new BusinessException(ReportErrorCode.NO_SUCH_REPORT));

    updateReportResult(report, request.getReportResult());

    Account processor = accountContextService.getContextAccount();
    updateProcessor(report, processor);

    reportJpaRepository.save(report);
    return report.getReportId();
  }


  private void updateReportResult(Report report, ReportResult reportResult) {
    if (reportResult != null) {
      if (report.getReportStatus() != ReportStatus.COMPLETED) {
        report.updateReportStatus(ReportStatus.COMPLETED);
      }
      report.updateReportResult(reportResult);
    }
  }

  private void updateProcessor(Report report, Account processor){
    report.updateProcessor(processor);
  }

}
