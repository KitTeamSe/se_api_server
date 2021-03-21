package com.se.apiserver.http.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AccountSignInDto {

  @Data
  @AllArgsConstructor
  @ApiModel("로그인 응답")
  static public class Response {

    @ApiModelProperty(notes = "jwt 토큰")
    private String token;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @ApiModel("로그인 요청")
  static public class Request {

    @ApiModelProperty(notes = "로그인 아이디", example = "user")
    @Size(min = 4, max = 20)
    private String id;
    @ApiModelProperty(notes = "로그인 비밀번호", example = "se75407540")
    private String pw;
  }
}
