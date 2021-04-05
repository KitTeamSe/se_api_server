package com.se.apiserver.v1.blacklist.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.account.domain.entity.Account;

import javax.persistence.*;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blacklist extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long blacklistId;

  @Column(length = 20, nullable = false)
  @Size(min = 4, max = 20)
  private String ip;

  @Column(length = 50, nullable = false)
  @Size(min = 4, max = 20)
  private String reason;

  public Blacklist(@Size(min = 4, max = 20) String ip, @Size(min = 4, max = 20) String reason) {
    this.ip = ip;
    this.reason = reason;
  }
}
