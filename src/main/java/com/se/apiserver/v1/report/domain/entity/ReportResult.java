package com.se.apiserver.v1.report.domain.entity;

public enum ReportResult {
  /* 반려 */
  REJECT,
  /* 승인 */
  TARGET_DELETE, IP_BAN, ID_BAN, POST_LIMIT, REPLY_LIMIT
}
