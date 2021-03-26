package com.se.apiserver.v1.authority.domain.entity;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorityGroupAccountMapping extends AccountGenerateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long authorityGroupAccountMappingId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "account_id", referencedColumnName = "accountId", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Account account;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "authority_group_id", referencedColumnName = "authorityGroupId", nullable = false)
  private AuthorityGroup authorityGroup;

  @Builder
  public AuthorityGroupAccountMapping(Long authorityGroupAccountMappingId, Account account, AuthorityGroup authorityGroup) {
    this.authorityGroupAccountMappingId = authorityGroupAccountMappingId;
    this.account = account;
    this.authorityGroup = authorityGroup;
  }
}
