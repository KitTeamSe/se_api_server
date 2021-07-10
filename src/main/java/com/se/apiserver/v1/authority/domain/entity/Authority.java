package com.se.apiserver.v1.authority.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority extends AccountGenerateEntity implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long authorityId;

  @Column(length = 40, unique = true)
  @Size(min = 2, max = 40)
  private String nameEng;

  @Column(length = 40, unique = true)
  @Size(min = 2, max = 40)
  private String nameKor;

  @OneToMany(mappedBy = "authority", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<AuthorityGroupAuthorityMapping> authorityGroupAuthorityMapping = new ArrayList<>();

  @Override
  public String getAuthority() {
    return nameEng;
  }

  public Authority(@Size(min = 2, max = 40) String nameEng, @Size(min = 2, max = 40) String nameKor) {
    this.nameEng = nameEng;
    this.nameKor = nameKor;
  }

  public void updateNameEng(String nameEng) {
    this.nameEng = nameEng;
  }

  public void updateNameKor(String nameKor) {
    this.nameKor = nameKor;
  }

  public void addAuthorityGroupAuthorityMapping(AuthorityGroupAuthorityMapping authorityGroupAuthorityMapping){
    this.authorityGroupAuthorityMapping.add(authorityGroupAuthorityMapping);
  }
}
