package com.se.apiserver.v1.common.domain.entity;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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

  @Column(length = 10)
  @Size(min = 2, max = 20)
  private String anonymousNickname;

  @Column(length = 255)
  @Size(min = 2, max = 255)
  @JsonIgnore
  private String anonymousPassword;


  @Builder
  public Anonymous(String anonymousNickname, String anonymousPassword) {
    this.anonymousNickname = anonymousNickname;
    this.anonymousPassword = anonymousPassword;
  }
}
