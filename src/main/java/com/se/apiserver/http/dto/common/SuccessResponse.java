package com.se.apiserver.http.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class SuccessResponse<E> {

  private int code;
  private String message;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private E data;

  public SuccessResponse(int code, String message, E data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public SuccessResponse(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
