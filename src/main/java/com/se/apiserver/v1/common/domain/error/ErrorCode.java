package com.se.apiserver.v1.common.domain.error;

public interface ErrorCode {

  String getCode();

  String getMessage();

  int getStatus();

}
