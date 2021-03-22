package com.se.apiserver.v1.authority.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Authority extends BaseEntity implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long authorityId;

  @Column(length = 30)
  private String nameEng;

  @Column(length = 30)
  private String nameKor;

  @Override
  public String getAuthority() {
    return nameEng;
  }

  @Builder
  public Authority(Long authorityId, String nameEng, String nameKor) {
    this.authorityId = authorityId;
    this.nameEng = nameEng;
    this.nameKor = nameKor;
  }
}
