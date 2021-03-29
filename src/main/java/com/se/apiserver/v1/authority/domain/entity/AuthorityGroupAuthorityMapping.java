package com.se.apiserver.v1.authority.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;

import javax.persistence.*;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorityGroupAuthorityMapping extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long authorityGroupAuthorityMappingId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "authority_id", referencedColumnName = "authorityId")
  private Authority authority;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "authority_group_id", referencedColumnName = "authorityGroupId")
  private AuthorityGroup authorityGroup;

  @Builder
  public AuthorityGroupAuthorityMapping(Long authorityGroupAuthorityMappingId, Authority authority, AuthorityGroup authorityGroup) {
    this.authorityGroupAuthorityMappingId = authorityGroupAuthorityMappingId;
    this.authority = authority;
    this.authorityGroup = authorityGroup;
  }
}
