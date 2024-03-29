package com.se.apiserver.v1.menu.domain.entity;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Size;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.application.error.MenuErrorCode;
import lombok.*;
import org.springframework.security.access.AccessDeniedException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "parent")
public class Menu extends AccountGenerateEntity {
  static private final String MANAGE_AUTHORITY = "MENU_MANAGE";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long menuId;

  @Column(length = 40, nullable = false, unique = true)
  @Size(min = 2, max = 20)
  private String nameEng;

  @Column(unique = true)
  @Size(min = 2, max = 255)
  private String url;

  @Column(length = 40, nullable = false, unique = true)
  @Size(min = 2, max = 20)
  private String nameKor;

  @Column(nullable = false)
  private Integer menuOrder;

  @Column
  @Size(max = 255)
  private String description;

  @Column(length = 20, nullable = false)
  @Enumerated(EnumType.STRING)
  private MenuType menuType;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "parent_id")
  private Menu parent;

  @OneToMany(mappedBy = "parent",fetch = FetchType.LAZY)
  private List<Menu> child = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinColumn(name = "access_authority_id")
  private Authority accessAuthority;

  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinColumn(name = "manage_authority_id")
  private Authority manageAuthority;

  @OneToOne(mappedBy = "menu", orphanRemoval = true, cascade = CascadeType.ALL)
  private Board board;


  public Menu(@Size(min = 2, max = 20) String nameEng, @Size(min = 2, max = 255) String url,
              @Size(min = 2, max = 20) String nameKor, Integer menuOrder,
              @Size(max = 255) String description, MenuType menuType) {
    this.nameEng = nameEng;
    this.url = url;
    this.nameKor = nameKor;
    this.menuOrder = menuOrder;
    this.description = description;
    this.menuType = menuType;
    if(parent != null)
      updateParent(parent);
    updateAccessAuthority(new Authority(nameEng + "_ACCESS", nameKor + "_접근"));
    updateManageAuthority(new Authority(nameEng + "_MANAGE", nameKor + "_관리"));
  }

  public Menu(@Size(min = 2, max = 20) String nameEng, @Size(min = 2, max = 255) String url,
              @Size(min = 2, max = 20) String nameKor, Integer menuOrder,
              @Size(min = 2, max = 255) String description, MenuType menuType, Menu parent) {
    this(nameEng, url, nameKor, menuOrder, description, menuType);
    if(parent != null)
      updateParent(parent);
  }

  public void updateNameEng(String nameEng) {
    this.nameEng = nameEng;
    this.accessAuthority.updateNameEng(nameEng + "_ACCESS");
    this.manageAuthority.updateNameEng(nameEng + "_MANAGE");
  }

  public void updateNameKor(String nameKor) {
    this.nameKor = nameKor;
    this.accessAuthority.updateNameKor(nameKor + "_접근");
    this.manageAuthority.updateNameKor(nameKor + "_관리");
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
    validateOccurCycle(parent);
    this.parent = parent;
    parent.getChild().add(this);
  }

  private void validateOccurCycle(Menu toBeParent) {
    if(dfs(this, toBeParent))
      throw new BusinessException(MenuErrorCode.OCCUR_CYCLE);
  }

  private boolean dfs(Menu now, Menu notTobeChild) {
    if(now == notTobeChild)
      return true;
    for(Menu child : now.getChild()){
      return dfs(child, notTobeChild);
    }
    return false;
  }

  public void updateUrl(String url) {
    this.url = url;
  }

  public void updateAccessAuthority(Authority authority){
    this.accessAuthority = authority;
  }

  public void updateManageAuthority(Authority authority){
    this.manageAuthority = authority;
  }

  public void validateAccessAuthority(Set<String> authorities) {
    if(!authorities.contains(this.getAccessAuthority().getAuthority())
        && !authorities.contains(this.getManageAuthority().getAuthority())
        && !authorities.contains(MANAGE_AUTHORITY))
      throw new AccessDeniedException("접근 권한이 없습니다");
  }

  public void validateManageAuthority(Set<String> authorities) {
    if(!authorities.contains(this.getManageAuthority().getAuthority()) && !authorities.contains(MANAGE_AUTHORITY))
      throw new AccessDeniedException("관리 권한이 없습니다");
  }

  public boolean canAccess(Set<String> authorities){
    if(!authorities.contains(this.getAccessAuthority().getAuthority()) && !authorities.contains(this.getManageAuthority().getAuthority()) && !authorities.contains(MANAGE_AUTHORITY))
      return false;
    return true;
  }

  public boolean isRemovable() {
    if(child.size() > 0)
      return false;
    return true;
  }
}
