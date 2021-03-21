package com.se.apiserver.domain.entity.authority;

import com.se.apiserver.domain.entity.BaseEntity;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
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
}
