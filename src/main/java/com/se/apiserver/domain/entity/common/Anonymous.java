package com.se.apiserver.domain.entity.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;

@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Anonymous {

  @Column(length = 20)
  private String anonymousId;

  @Column(length = 10)
  private String anonymousNickname;

  @Column(length = 20)
  private String anonymousPassword;

  @Column(length = 30)
  @Email
  private String anonymousEmail;

  @Builder
  public Anonymous(String anonymousId, String anonymousNickname, String anonymousPassword,
      @Email String anonymousEmail) {
    this.anonymousId = anonymousId;
    this.anonymousNickname = anonymousNickname;
    this.anonymousPassword = anonymousPassword;
    this.anonymousEmail = anonymousEmail;
  }
}
