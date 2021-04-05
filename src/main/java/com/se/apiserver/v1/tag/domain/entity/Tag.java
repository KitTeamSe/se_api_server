package com.se.apiserver.v1.tag.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountReceiveTagMapping;
import com.se.apiserver.v1.post.domain.entity.PostTagMapping;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends AccountGenerateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long tagId;


  @Column(length = 30, nullable = false, unique = true)
  @Size(min = 1, max = 30)
  private String text;

  public Tag(@Size(min = 1, max = 30) String text) {
    this.text = text;
  }

  public void updateText(String text) {
    this.text = text;
  }
}
