package com.se.apiserver.v1.tag.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.post.domain.entity.PostTagMapping;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends AccountGenerateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long tagId;

  @Column(columnDefinition = "VARCHAR(30) UNIQUE NOT NULL, FULLTEXT KEY textFulltext (text)")
  @Size(min = 1, max = 30)
  private String text;

  @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<PostTagMapping> postTagMapping = new ArrayList<>();

  public Tag(@Size(min = 1, max = 30) String text) {
    this.text = text;
  }

  public void updateText(String text) {
    this.text = text;
  }
}
