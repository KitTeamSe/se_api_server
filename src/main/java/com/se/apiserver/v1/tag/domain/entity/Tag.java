package com.se.apiserver.v1.tag.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;

import com.se.apiserver.v1.post.domain.entity.Post;
import java.util.HashSet;
import java.util.Set;
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

  @Column(columnDefinition = "VARCHAR(30) UNIQUE NOT NULL, FULLTEXT KEY textFulltext (text) WITH PARSER ngram")
  @Size(min = 1, max = 30)
  private String text;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "post_tag"
      , joinColumns = @JoinColumn(name = "tag_id")
      , inverseJoinColumns = @JoinColumn(name = "post_id"))
  private Set<Post> posts = new HashSet<>();

  public Tag(@Size(min = 1, max = 20) String text) {
    this.text = text;
  }

  public void updateText(String text) {
    this.text = text;
  }
}
