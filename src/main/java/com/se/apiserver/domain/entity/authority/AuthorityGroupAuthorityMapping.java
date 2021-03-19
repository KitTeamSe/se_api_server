package com.se.apiserver.domain.entity.authority;

import com.se.apiserver.domain.entity.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class AuthorityGroupAuthorityMapping extends BaseEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long authorityGroupAuthorityMappingId;

  @ManyToOne
  @JoinColumn(name = "authority_id", referencedColumnName = "authorityId")
  private Authority authority;

  @ManyToOne
  @JoinColumn(name = "authority_group_id", referencedColumnName = "authorityGroupId")
  private AuthorityGroup authorityGroup;
}
