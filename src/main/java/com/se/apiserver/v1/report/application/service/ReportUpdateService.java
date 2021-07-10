package com.se.apiserver.v1.report.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.report.application.dto.ReportUpdateDto;
import com.se.apiserver.v1.report.application.error.ReportErrorCode;
import com.se.apiserver.v1.report.domain.entity.Report;
import com.se.apiserver.v1.report.domain.entity.ReportStatus;
import com.se.apiserver.v1.report.infra.repository.ReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportUpdateService {

  private final AccountContextService accountContextService;
  private final ReportJpaRepository reportJpaRepository;
  private final AccountJpaRepository accountJpaRepository;

  @Transactional
  public Long update(ReportUpdateDto.Request request) {

    Report report = reportJpaRepository
        .findById(request.getReportId())
        .orElseThrow(() -> new BusinessException(ReportErrorCode.NO_SUCH_REPORT));

    if(request.getDescription() != null){
      updateDescription(report, request.getDescription());
    }

    if(request.getReportStatus() != null){
      updateReportStatus(report, request.getReportStatus());
    }

    Account processor = accountContextService.getContextAccount();
    updateProcessor(report, processor);

    return reportJpaRepository.save(report).getReportId();
  }

  public void updateDescription(Report report, String description){
    report.updateDescription(description);
  }

  public void updateReportStatus(Report report, ReportStatus reportStatus){
    report.updateReportStatus(reportStatus);
  }

  public void updateProcessor(Report report, Account processor){
    report.updateProcessor(processor);
  }
}
