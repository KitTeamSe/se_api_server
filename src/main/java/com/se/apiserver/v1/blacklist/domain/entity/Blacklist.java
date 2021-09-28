package com.se.apiserver.v1.blacklist.domain.entity;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.entity.BaseEntity;

import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blacklist extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long blacklistId;

  @Column(length = 20)
  @Size(min = 4, max = 20)
  private String ip;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", referencedColumnName = "accountId")
  private Account account;

  @Column(length = 50, nullable = false)
  @Size(min = 4, max = 20)
  private String reason;

  @Column
  private LocalDateTime releaseDate;

  public Blacklist(String ip, Account account, String reason, LocalDateTime releaseDate) {
    this.ip = ip;
    this.account = account;
    this.reason = reason;
    this.releaseDate = releaseDate;
  }
}
