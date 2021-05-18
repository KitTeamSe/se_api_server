package com.se.apiserver.v1.report.domain.entity;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.report.application.error.ReportErrorCode;

public enum ReportType {
  POST(Values.POST),
  REPLY(Values.REPLY);

  private String name;

  ReportType(String name){
    if (!this.name().equals(name))
      throw new BusinessException(ReportErrorCode.NO_SUCH_REPORT_TYPE);
  }

  public static class Values{
    public static final String POST = "POST";
    public static final String REPLY = "REPLY";
  }
}
