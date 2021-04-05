package com.se.apiserver.v1.account.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AccountFindPasswordDto {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @ApiModel("비밀번호 찾기 요청")
  static public class Request {

    @ApiModelProperty(notes = "사용자 아이디", example = "user")
    @Size(min = 4, max = 20)
    private String id;

    @ApiModelProperty(notes = "사용자 이메일", example = "djh20@naver.com")
    @Email
    @Size(min = 4, max = 40)
    private String email;

    @ApiModelProperty(notes = "질문 번호(fe 제공)", example = "4")
    private Long questionId;

    @ApiModelProperty(notes = "응답", example = "구미")
    @Size(min = 2, max = 200)
    private String answer;
  }

}
