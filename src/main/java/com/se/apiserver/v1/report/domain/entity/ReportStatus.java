package com.se.apiserver.v1.report.domain.entity;

public enum ReportStatus {
  SUBMITTED,  // 제출(처리전)
  REVIEWING,  // 읽음(처리중)
  REJECTED,   // 반려(처리완료 -> ReportResult = null)
  ACCEPTED,   // 승인(처리완료 -> ReportResult = has value)
}
