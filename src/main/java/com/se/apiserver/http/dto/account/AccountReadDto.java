package com.se.apiserver.http.dto.account;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

public class AccountReadDto {

  @Data
  @AllArgsConstructor
  static public class Response {
    private String id;
    private String pw;
  }
}
