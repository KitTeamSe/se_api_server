package com.se.apiserver.v1.tag.domain.entity;

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
public class Tag extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long tagId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "account_id", referencedColumnName = "accountId", nullable = false)
  private Account registrantId;

  @Column(length = 30, nullable = false)
  @Size(min = 1, max = 30)
  private String text;

  @OneToMany(mappedBy = "tag", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<PostTagMapping> posts = new ArrayList<>();

  @OneToMany(mappedBy = "tag", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<AccountReceiveTagMapping> accounts = new ArrayList<>();

  @Builder
  public Tag(Long tagId, Account registrantId, @Size(min = 1, max = 30) String text, List<PostTagMapping> posts,
      List<AccountReceiveTagMapping> accounts) {
    this.tagId = tagId;
    this.registrantId = registrantId;
    this.text = text;
    this.posts = posts;
    this.accounts = accounts;
  }
}
