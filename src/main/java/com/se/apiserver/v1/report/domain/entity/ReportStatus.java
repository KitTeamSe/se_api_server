package com.se.apiserver.v1.report.domain.entity;

public enum ReportStatus {
  SUBMITTED,  // 제출(처리전)
  REVIEWING,  // 읽음(처리중)
  COMPLETED    // 처리됨(처리완료)
}
