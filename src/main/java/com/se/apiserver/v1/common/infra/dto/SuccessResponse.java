package com.se.apiserver.v1.common.infra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class SuccessResponse<E> {

  private int status;
  private String code;
  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private E data;

  public SuccessResponse(int status, String message) {
    this.status = status;
    this.message = message;
    this.code = "SR01";
  }

  public SuccessResponse(int status, String message, E data) {
    this(status, message);
    this.data = data;
  }

  public SuccessResponse(int status, String message, String code) {
    this(status, message);
    this.code = code;
  }

  public SuccessResponse(int status, String message, String code, E data) {
    this(status, message, data);
    this.code = code;
  }
}
