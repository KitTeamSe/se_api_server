package com.se.apiserver.v1.account.application.dto;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

public class AccountFindIdByEmailDto {

  @Data
  @AllArgsConstructor
  @ApiModel(value = "아이디 찾기 응답")
  static public class Response{
    @Size(min = 4, max = 20)
    String id;
  }

}
