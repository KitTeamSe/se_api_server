package com.se.apiserver.v1.report.infra.repository;

import com.querydsl.jpa.JPQLQuery;
import com.se.apiserver.v1.report.application.dto.ReportReadDto.ReportSearchRequest;
import com.se.apiserver.v1.report.domain.entity.QReport;
import com.se.apiserver.v1.report.domain.entity.Report;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class ReportQueryRepositoryImpl extends QuerydslRepositorySupport implements ReportQueryRepository {
  public ReportQueryRepositoryImpl() {super(Report.class);}

  @Override
  public Page<Report> search(ReportSearchRequest request) {

    QReport report = QReport.report;
    JPQLQuery query = from(report);

    if (request.getDescription() != null) {
      query.where(report.description.contains(request.getDescription()));
    }
    if (request.getReportStatus() != null) {
      query.where(report.reportStatus.eq(request.getReportStatus()));
    }
    if (request.getReportResult() != null) {
      query.where(report.reportResult.eq(request.getReportResult()));
    }
    if (request.getProcessorIdString() != null) {
      query.where(report.processor.idString.contains(request.getProcessorIdString()));
    }
    if (request.getReporterIdString() != null) {
      query.where(report.reporter.idString.contains(request.getReporterIdString()));
    }

    Pageable pageable = request.getPageRequest().of();
    List<Report> reportList = getQuerydsl().applyPagination(pageable, query).fetch();
    long totalCount = query.fetchCount();

    return new PageImpl(reportList, pageable, totalCount);
  }
}
