package com.se.apiserver.v1.account.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountVerifyToken extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long accountVerifyUrl;

  @Column(length = 40)
  @Email
  private String email;

  @Column(length = 255, unique = true)
  private String token;

  private LocalDateTime timeExpire;

  @Column(length = 30)
  @Enumerated(EnumType.STRING)
  private AccountVerifyStatus status;

  public AccountVerifyToken(String email, String token,
      LocalDateTime timeExpire, AccountVerifyStatus status) {
    this.email = email;
    this.token = token;
    this.timeExpire = timeExpire;
    this.status = status;
  }

  public void verify() {
    this.status = AccountVerifyStatus.VERIFIED;
  }
}
