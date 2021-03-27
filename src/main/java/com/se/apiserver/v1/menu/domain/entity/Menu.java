package com.se.apiserver.v1.menu.domain.entity;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.domain.error.MenuErrorCode;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "parent")
public class Menu extends AccountGenerateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long menuId;

  @Column(length = 40, nullable = false, unique = true)
  @Size(min = 2, max = 20)
  private String nameEng;

  @Column(length = 255, nullable = false, unique = true)
  @Size(min = 2, max = 255)
  private String url;

  @Column(length = 40, nullable = false, unique = true)
  @Size(min = 2, max = 20)
  private String nameKor;

  @Column(nullable = false)
  private Integer menuOrder;

  @Column(length = 255, nullable = false)
  @Size(min = 2, max = 255)
  private String description;

  @Column(length = 20, nullable = false)
  private MenuType menuType;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "parent_id")
  private Menu parent;

  @OneToMany(mappedBy = "parent",fetch = FetchType.LAZY)
  private List<Menu> child = new ArrayList<>();

  @OneToOne(mappedBy = "menu", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
  private Authority authority;

  @Builder
  public Menu(@Size(min = 2, max = 20) String nameEng, @Size(min = 2, max = 255) String url, @Size(min = 2, max = 20) String nameKor, Integer menuOrder, @Size(min = 2, max = 50) String description, @Size(min = 2, max = 20) MenuType menuType) {
    this.nameEng = nameEng;
    this.url = url;
    this.nameKor = nameKor;
    this.menuOrder = menuOrder;
    this.description = description;
    this.menuType = menuType;
  }

  public void updateNameEng(String nameEng) {
    this.nameEng = nameEng;
  }

  public void updateNameKor(String nameKor) {
    this.nameKor = nameKor;
  }

  public void updateDescription(String description) {
    this.description = description;
  }

  public void updateMenuOrder(Integer menuOrder) {
    this.menuOrder = menuOrder;
  }

  public void updateParent(Menu parent){
    if(parent.getMenuType() != MenuType.FOLDER)
      throw new BusinessException(MenuErrorCode.ONLY_FOLDER_MENU_CAN_BE_PARENT);
    this.parent = parent;
    parent.getChild().add(this);
  }

  public void updateUrl(String url) {
    this.url = url;
  }

  public void updateMenuType(MenuType menuType) {
    if(this.menuType == MenuType.FOLDER && menuType != MenuType.FOLDER && child.size() > 0)
      throw new BusinessException(MenuErrorCode.CHILD_REMOVE_FIRST);
    this.menuType = menuType;
  }

  public void updateAuthority(Authority authority){
    this.authority = authority;
    if(authority.getMenu() != this)
      authority.updateMenu(this);
  }
}