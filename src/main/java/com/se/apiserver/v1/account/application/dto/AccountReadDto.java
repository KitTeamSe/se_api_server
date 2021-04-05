package com.se.apiserver.v1.account.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;

import javax.validation.constraints.NotNull;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
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
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static public class SearchRequest{
    private String name;
    private String nickname;
    private String email;
    private String studentId;
    private String phoneNumber;
    private AccountType type;

    @NotNull
    private PageRequest pageRequest;
  }
}
