package com.se.apiserver.v1.account.application.dto;

import com.se.apiserver.v1.account.domain.entity.AccountType;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

public class AccountCreateDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @ApiModel("회원 가입 요청")
  @Builder
  static public class Request {

    @ApiModelProperty(example = "account", notes = "아이디")
    @Size(min = 4, max = 20)
    private String id;

    @ApiModelProperty(example = "password", notes = "비밀번호")
    @Size(min = 8, max = 20)
    private String password;

    @ApiModelProperty(example = "name", notes = "이름")
    @Size(min = 2, max = 20)
    private String name;

    @ApiModelProperty(example = "nickname", notes = "닉네임")
    @Size(min = 2, max = 20)
    private String nickname;

    @ApiModelProperty(example = "11110000", notes = "학번")
    @Size(min = 8, max = 20)
    private String studentId;

    @ApiModelProperty(example = "STUDENT", notes = "사용자 타입")
    private AccountType type;

    @ApiModelProperty(example = "01012345678", notes = "전화번호, 00011112222 형식")
    @Size(min = 10, max = 20)
    private String phoneNumber;

    @ApiModelProperty(example = "abc@def.com", notes = "이메일")
    @Email
    private String email;

    @ApiModelProperty(example = "1", notes = "질문 번호")
    private Long questionId;

    @ApiModelProperty(example = "구미", notes = "답변")
    @Size(min = 2, max = 100)
    private String answer;


  }

  @Data
  @AllArgsConstructor
  @ApiModel("회원가입 응답")
  static public class Response {

    @ApiModelProperty(example = "1", notes = "사용자 pk")
    private Long id;
  }

}
