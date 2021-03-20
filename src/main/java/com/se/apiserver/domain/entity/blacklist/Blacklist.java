package com.se.apiserver.domain.entity.blacklist;

import com.se.apiserver.domain.entity.BaseEntity;
import com.se.apiserver.domain.entity.account.Account;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

  @ManyToOne
  @JoinColumn(name = "accountId", nullable = false)
  private Account registrant;

  @Column(length = 20, nullable = false)
  @Size(min = 4, max = 20)
  private String ip;

  @Column(length = 50, nullable = false)
  @Size(min = 4, max = 20)
  private String reason;

  @Builder
  public Blacklist(Long blacklistId, Account registrant, @Size(min = 4, max = 20) String ip, @Size(min = 4, max = 20) String reason) {
    this.blacklistId = blacklistId;
    this.registrant = registrant;
    this.ip = ip;
    this.reason = reason;
  }
}
