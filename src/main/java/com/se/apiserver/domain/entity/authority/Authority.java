package com.se.apiserver.domain.entity.authority;

import com.se.apiserver.domain.entity.BaseEntity;
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
@AllArgsConstructor
public class Authority extends BaseEntity implements GrantedAuthority {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long authorityId;

  @Column(length = 30)
  private String nameEng;

  @Column(length = 30)
  private String nameKor;

  @Override
  public String getAuthority() {
    return nameEng;
  }
}
