package com.se.apiserver.v1.post.domain.entity;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.reply.domain.entity.Reply;
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
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long postId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "board_id", referencedColumnName = "boardId", nullable = false)
  private Board board;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "account_id", referencedColumnName = "accountId")
  private Account account;

  @Column(length = 50, nullable = false)
  @Size(min = 3, max = 50)
  private String title;

  @Column(length = 2000, nullable = false)
  @Size(min = 5, max = 2000)
  private String text;

  @Embedded
  private Anonymous anonymous;

  @Column(length = 10, nullable = false)
  @Enumerated(EnumType.STRING)
  private PostIsNotice isNotice;

  @Column(nullable = false)
  private Integer views;

  @Column(length = 10, nullable = false)
  @Enumerated(EnumType.STRING)
  private PostIsDeleted isDeleted;

  @Column(length = 10, nullable = false)
  @Enumerated(EnumType.STRING)
  private PostIsSecret isSecret;

  @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Reply> replies = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Attach> attaches = new ArrayList<>();

  @Builder
  public Post(Board board, Account account, @Size(min = 3, max = 50) String title,
      @Size(min = 5, max = 2000) String text, Anonymous anonymous,
      PostIsNotice isNotice, Integer views, PostIsDeleted isDeleted,
      PostIsSecret isSecret, List<Reply> replies, List<Attach> attaches) {
    this.board = board;
    this.account = account;
    this.title = title;
    this.text = text;
    this.anonymous = anonymous;
    this.isNotice = isNotice;
    this.views = views;
    this.isDeleted = isDeleted;
    this.isSecret = isSecret;
    this.replies = replies;
    this.attaches = attaches;
  }
}
