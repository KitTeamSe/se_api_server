package com.se.apiserver.domain.entity.menu;

import com.se.apiserver.domain.entity.BaseEntity;
import java.util.ArrayList;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long menuId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Menu parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private List<Menu> child;

  @Column(length = 10)
  @Size(min = 2, max = 10)
  private String name;

  @Column
  private Integer menuOrder;

  @Column(length = 50)
  @Size(min = 2, max = 50)
  private String description;

  @Column(length = 255)
  @Size(min = 4, max = 255)
  private String url;
}
