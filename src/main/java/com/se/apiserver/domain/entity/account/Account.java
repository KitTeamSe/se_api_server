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
@AllArgsConstructor
@Builder
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

  @Column(nullable = false)
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

  @Column(nullable = false)
  private InformationOpenAgree informationOpenAgree;


}
