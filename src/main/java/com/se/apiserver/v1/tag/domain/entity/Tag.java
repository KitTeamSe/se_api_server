package com.se.apiserver.v1.tag.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;

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

  public static final String TAG_AUTHORITY = "TAG_MANAGE";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long tagId;

  @Column(columnDefinition = "VARCHAR(30) UNIQUE NOT NULL, FULLTEXT KEY textFulltext (text) WITH PARSER ngram")
  @Size(min = 1, max = 30)
  private String text;

  public Tag(@Size(min = 1, max = 20) String text) {
    this.text = text;
  }

  public void updateText(String text) {
    this.text = text;
  }

  public final boolean hasManageAuthority(Set<String> authorities) {
    return authorities.contains(TAG_AUTHORITY);
  }
}
