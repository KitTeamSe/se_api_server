package com.se.apiserver.domain.entity.blacklist;

import com.se.apiserver.domain.entity.BaseEntity;
import com.se.apiserver.domain.entity.account.Account;

import javax.persistence.*;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blacklist extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long blacklistId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "accountId", nullable = false)
  private Account registrant;

  @Column(length = 20, nullable = false)
  @Size(min = 4, max = 20)
  private String ip;

  @Column(length = 50, nullable = false)
  @Size(min = 4, max = 20)
  private String reason;

  @Builder
  public Blacklist(Long blacklistId, Account registrant, @Size(min = 4, max = 20) String ip,
      @Size(min = 4, max = 20) String reason) {
    this.blacklistId = blacklistId;
    this.registrant = registrant;
    this.ip = ip;
    this.reason = reason;
  }
}
