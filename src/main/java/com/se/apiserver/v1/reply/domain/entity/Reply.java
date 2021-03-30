package com.se.apiserver.v1.reply.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.post.domain.entity.Post;
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long replyId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @Column(length = 500, nullable = false)
  @Size(min = 4, max = 500)
  private String text;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "account_id", referencedColumnName = "accountId")
  private Account account;

  @Embedded
  private Anonymous anonymous;

  @Column(length = 20, nullable = false)
  @Size(min = 4, max = 20)
  private String ip;

  @Column(length = 10, nullable = false)
  @Size(min = 2, max = 10)
  @Enumerated(EnumType.STRING)
  private ReplyIsDelete status;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "parent_id")
  private Reply parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Reply> child;

  @OneToMany(mappedBy = "reply", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Attach> attaches = new ArrayList<>();

  public void addAttach(Attach attach) {
    attaches.add(attach);
  }
  
}
