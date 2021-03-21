package com.se.apiserver.domain.error;

public interface ErrorCode {

  String getCode();

  String getMessage();

  int getStatus();

}
