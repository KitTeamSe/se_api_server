package com.se.apiserver.v1.report.infra.repository;

import com.se.apiserver.v1.report.application.dto.ReportReadDto.ReportSearchRequest;
import com.se.apiserver.v1.report.domain.entity.Report;
import org.springframework.data.domain.Page;

public interface ReportQueryRepository {
  Page<Report> search(ReportSearchRequest request);
}
