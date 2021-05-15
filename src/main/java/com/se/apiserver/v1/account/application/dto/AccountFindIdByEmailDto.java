package com.se.apiserver.v1.account.application.dto;

import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

public class AccountFindIdByEmailDto {

  @Data
  @AllArgsConstructor
  static public class Response{
    @Size(min = 4, max = 20)
    String id;
  }

}
