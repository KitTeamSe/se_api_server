package com.se.apiserver.domain.entity.account;

import com.se.apiserver.domain.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long accountId;

  @Size(min = 5, max = 20)
  @Column(nullable = false, unique = true)
  private String idString;

  @Column(nullable = false)
  private String password;

  @Size(min = 2, max = 20)
  @Column(nullable = false)
  private String name;

  @Size(min = 2, max = 20)
  @Column(nullable = false)
  private String nickname;

  @Size(min = 8, max = 20)
  @Column(nullable = false)
  private String studentId;

  @Column(nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private AccountType type;

  @Size(min = 10, max = 20)
  @Column
  private String phoneNumber;

  @Size(min = 4, max = 40)
  @Column(nullable = false)
  private String email;

  @Column(length = 20)
  @Size(min = 4, max = 20)
  private String lastSignInIp;

  @Column(nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private InformationOpenAgree informationOpenAgree;

  @Builder
  public Account(Long accountId, @Size(min = 5, max = 20) String idString, String password, @Size(min = 2, max = 20) String name, @Size(min = 2, max = 20) String nickname, @Size(min = 8, max = 20) String studentId, AccountType type, @Size(min = 10, max = 20) String phoneNumber, @Size(min = 4, max = 40) String email, @Size(min = 4, max = 20) String lastSignInIp, InformationOpenAgree informationOpenAgree) {
    this.accountId = accountId;
    this.idString = idString;
    this.password = password;
    this.name = name;
    this.nickname = nickname;
    this.studentId = studentId;
    this.type = type;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.lastSignInIp = lastSignInIp;
    this.informationOpenAgree = informationOpenAgree;
  }
}
