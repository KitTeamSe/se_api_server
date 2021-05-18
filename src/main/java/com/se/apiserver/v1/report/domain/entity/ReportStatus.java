package com.se.apiserver.v1.report.domain.entity;

public enum ReportStatus {
  // 제출, 검토, 승인, 거부, 보류
  SUBMITTED, REVIEWING, ACCEPTED, REJECTED, POSTPONED;
}
