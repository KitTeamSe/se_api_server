package com.se.apiserver.http.dto.account;

import com.se.apiserver.domain.entity.account.AccountType;
import com.se.apiserver.domain.entity.account.InformationOpenAgree;
import javax.persistence.Column;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

public class AccountCreateDto {

  @Data
  static public class Request{
    @Size(min = 5, max = 20)
    private String id;

    private String password;

    @Size(min = 2, max = 20)
    private String name;

    @Size(min = 2, max = 20)
    private String nickname;

    @Size(min = 8, max = 20)
    private String studentId;

    private AccountType type;

    @Size(min = 10, max = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Size(min = 4, max = 20)
    private String lastSignInIp;

    private InformationOpenAgree informationOpenAgree;

  }

  @Data
  @AllArgsConstructor
  static public class Response{
      private Long id;
  }

}
