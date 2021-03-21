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
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class AccountReadDto {

  @Data
  @AllArgsConstructor
  static public class ReadResponse {

    @Size(min = 4, max = 20)
    private String id;
    private String pw;
  }

  @Data
  @Builder
  static public class ReadAllResponse {

    @Size(min = 4, max = 20)
    private String idString;

    private String name;

    private String nickname;

    private String studentId;

    private AccountType type;

    private String phoneNumber;

    private String email;

    private InformationOpenAgree informationOpenAgree;

    @JsonInclude(Include.NON_NULL)
    private String lastSignInIp;

    @JsonInclude(Include.NON_NULL)
    private Long accountId;

    public static ReadAllResponse create(Account account, boolean hasManageAuthority) {
      ReadAllResponseBuilder res = ReadAllResponse.builder()
          .idString(account.getIdString())
          .name(account.getName())
          .nickname(account.getNickname())
          .type(account.getType())
          .phoneNumber(account.getPhoneNumber())
          .email(account.getEmail())
          .informationOpenAgree(account.getInformationOpenAgree());

      if (hasManageAuthority) {
        res.lastSignInIp(account.getLastSignInIp())
            .accountId(account.getAccountId());
      }
      return res.build();
    }
  }
}
