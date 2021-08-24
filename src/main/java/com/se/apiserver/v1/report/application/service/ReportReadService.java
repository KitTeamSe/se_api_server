package com.se.apiserver.v1.report.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.report.application.dto.ReportReadDto;
import com.se.apiserver.v1.report.application.dto.ReportReadDto.Response;
import com.se.apiserver.v1.report.application.error.ReportErrorCode;
import com.se.apiserver.v1.report.domain.entity.Report;
import com.se.apiserver.v1.report.domain.entity.ReportStatus;
import com.se.apiserver.v1.report.infra.repository.ReportJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReportReadService {

  private final ReportJpaRepository reportJpaRepository;

  public ReportReadService(
      ReportJpaRepository reportJpaRepository) {
    this.reportJpaRepository = reportJpaRepository;
  }

  @Transactional
  public ReportReadDto.Response read(Long id){
    Report report = reportJpaRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ReportErrorCode.NO_SUCH_REPORT));

    if (report.getReportStatus() == ReportStatus.SUBMITTED) {
      report.updateReportStatus(ReportStatus.REVIEWING);
      reportJpaRepository.save(report);
    }

    return ReportReadDto.Response.fromEntity(report);
  }

  public PageImpl readAll(Pageable pageable){
    Page<Report> reportPage = reportJpaRepository.findAll(pageable);
    List<Response> responseList = reportPage.stream()
        .map(response -> ReportReadDto.Response.fromEntity(response))
        .collect(Collectors.toList());
    return new PageImpl(responseList, reportPage.getPageable(), reportPage.getTotalElements());
  }

  //public

}
