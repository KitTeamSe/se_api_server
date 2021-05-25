package com.se.apiserver.v1.report.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.report.application.error.ReportErrorCode;
import com.se.apiserver.v1.report.domain.entity.Report;
import com.se.apiserver.v1.report.infra.repository.ReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportDeleteService {
  private final ReportJpaRepository reportJpaRepository;

  @Transactional
  public void delete(Long reportId){
    Report report = reportJpaRepository
        .findById(reportId).orElseThrow(() -> new BusinessException(ReportErrorCode.NO_SUCH_REPORT));
    reportJpaRepository.delete(report);
  }
}
