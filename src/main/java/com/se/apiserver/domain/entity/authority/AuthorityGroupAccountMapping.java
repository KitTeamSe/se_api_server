package com.se.apiserver.domain.entity.authority;

import com.se.apiserver.domain.entity.account.Account;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class AuthorityGroupAccountMapping {

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
}
