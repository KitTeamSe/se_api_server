package com.se.apiserver.v1.account.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AccountReadDto {

  @Data
  @Builder
  static public class Response {

    private String idString;

    private String name;

    private String nickname;

    private String email;

    private AccountType type;

    @JsonInclude(Include.NON_NULL)
    private String phoneNumber;

    @JsonInclude(Include.NON_NULL)
    private String studentId;

    @JsonInclude(Include.NON_NULL)
    private InformationOpenAgree informationOpenAgree;

    @JsonInclude(Include.NON_NULL)
    private String lastSignInIp;

    @JsonInclude(Include.NON_NULL)
    private Long accountId;

    public static Response fromEntity(Account account, Boolean isLegalAccess) {
        Response.ResponseBuilder responseBuilder = Response.builder();
        responseBuilder
            .idString(account.getIdString())
            .name(account.getName())
            .nickname(account.getNickname())
            .email(account.getEmail())
            .type(account.getType());
        if (isLegalAccess) {
          responseBuilder
              .phoneNumber(account.getPhoneNumber())
              .studentId(account.getStudentId())
              .informationOpenAgree(account.getInformationOpenAgree())
              .lastSignInIp(account.getLastSignInIp())
              .accountId(account.getAccountId());
        }
        return responseBuilder.build();
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static public class AccountSearchRequest {

    @ApiModelProperty(notes = "????????? ??????", example = "????????? ?????????")
    private String name;

    @ApiModelProperty(notes = "????????? ?????????", example = "????????? ?????????")
    private String nickname;

    @ApiModelProperty(notes = "????????? ?????????", example = "????????? ?????????")
    private String email;

    @ApiModelProperty(notes = "????????? ??????", example = "????????? ?????????")
    private String studentId;

    @ApiModelProperty(notes = "????????? ????????? ??????", example = "????????? ?????????")
    private String phoneNumber;

    @ApiModelProperty(notes = "????????? ?????? ??????")
    private AccountType type;

    @NotNull
    private PageRequest pageRequest;
  }
}
