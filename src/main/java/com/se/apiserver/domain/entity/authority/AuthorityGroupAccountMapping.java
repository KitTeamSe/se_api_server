package com.se.apiserver.domain.entity.authority;

import com.se.apiserver.domain.entity.account.Account;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class AuthorityGroupAccountMapping {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorityGroupAccountMappingId;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "authority_group_id", referencedColumnName = "authorityGroupId", nullable = false)
    private AuthorityGroup authorityGroup;
}
