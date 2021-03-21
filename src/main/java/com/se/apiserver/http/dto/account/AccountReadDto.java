package com.se.apiserver.http.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.entity.account.AccountType;
import com.se.apiserver.domain.entity.account.InformationOpenAgree;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.se.apiserver.http.dto.common.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AccountReadDto {
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
}
