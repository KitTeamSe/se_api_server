package com.se.apiserver.v1.menu.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long menuId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "parent_id")
  private Menu parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Menu> child;

  @Column(length = 10, nullable = false)
  @Size(min = 2, max = 10)
  private String name;

  @Column(nullable = false)
  private Integer menuOrder;

  @Column(length = 50, nullable = false)
  @Size(min = 2, max = 50)
  private String description;

  @Column(length = 255, nullable = false)
  @Size(min = 4, max = 255)
  private String url;

  @Builder
  public Menu(Long menuId, Menu parent, List<Menu> child, @Size(min = 2, max = 10) String name, Integer menuOrder,
      @Size(min = 2, max = 50) String description, @Size(min = 4, max = 255) String url) {
    this.menuId = menuId;
    this.parent = parent;
    this.child = child;
    this.name = name;
    this.menuOrder = menuOrder;
    this.description = description;
    this.url = url;
  }
}
