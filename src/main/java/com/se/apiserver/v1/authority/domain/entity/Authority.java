package com.se.apiserver.v1.authority.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.entity.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;

import com.se.apiserver.v1.menu.domain.entity.Menu;
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

  @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
  @JoinColumn(name = "menu_id",referencedColumnName = "menuId")
  private Menu menu;


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

  public void updateMenu(Menu menu) {
    this.menu = menu;
    if(menu.getAuthority() != this)
      menu.updateAuthority(this);
  }

  public void updateNameEng(String nameEng) {
    this.nameEng = nameEng;
  }

  public void updateNameKor(String nameKor) {
    this.nameKor = nameKor;
  }
}
