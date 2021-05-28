package com.se.apiserver.v1.account.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountReceiveTagMapping extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long accountReceiveTagMappingId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "account_id", referencedColumnName = "accountId", nullable = false)
  private Account account;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "tag_id", referencedColumnName = "tagId", nullable = false)
  private Tag tag;

  @Builder
  public AccountReceiveTagMapping(Long accountReceiveTagMappingId, Account account, Tag tag) {
    this.accountReceiveTagMappingId = accountReceiveTagMappingId;
    this.account = account;
    this.tag = tag;
  }
}
