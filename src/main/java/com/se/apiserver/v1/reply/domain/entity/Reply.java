package com.se.apiserver.v1.reply.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.application.error.MenuErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;

import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseEntity {

  public static String MANAGE_AUTHORITY = "REPLY_MANAGE";
  public static String DELETED_REPLY_TEXT = "사용자에 의해 삭제된 댓글입니다.";
  public static String SECRET_REPLY_TEXT = "비밀 댓글입니다.";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long replyId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @Column(length = 500, nullable = false)
  @Size(min = 4, max = 500)
  private String text;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "account_id", referencedColumnName = "accountId")
  private Account account;

  @Embedded
  private Anonymous anonymous;

  @Column(length = 20, nullable = false)
  @Enumerated(EnumType.STRING)
  private ReplyIsDelete isDelete;

  @Column(length = 20, nullable = false)
  @Enumerated(EnumType.STRING)
  private ReplyIsSecret isSecret;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "parent_id")
  private Reply parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<Reply> child = new HashSet<>();

  @OneToMany(mappedBy = "reply", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<Attach> attaches = new HashSet<>();

  @Column(nullable = false, updatable = false)
  private String createdIp;

  @Column
  private String lastModifiedIp;

  public void addAttach(Attach attach) {
    attaches.add(attach);
  }

  private Reply(Post post, String text, ReplyIsSecret isSecret, List<Attach> attaches, Reply parent,
      String ip) {
    setPost(post);
    this.text = text;
    this.isDelete = ReplyIsDelete.NORMAL;
    this.isSecret = isSecret;
    updateAttaches(attaches);
    updateParent(parent);
    this.createdIp = ip;
  }

  public Reply(Post post, String text, ReplyIsSecret isSecret, List<Attach> attaches, Reply parent,
      String ip, Anonymous anonymous) {
    this(post, text, isSecret, attaches, parent, ip);
    validateAnonymousInput(anonymous);
    this.anonymous = anonymous;
  }

  public Reply(Post post, String text, ReplyIsSecret isSecret, List<Attach> attaches, Reply parent,
      String ip, Account account) {
    this(post, text, isSecret, attaches, parent, ip);
    this.account = account;
  }

  private void setPost(Post post) {
    this.post = post;
    post.addReply(this);
  }

  public void updateAttaches(List<Attach> attaches) {
    if (attaches == null) {
      return;
    }
    this.attaches = new HashSet<>(attaches);
  }

  private void validateAnonymousInput(Anonymous anonymous) {
    if (anonymous.getAnonymousPassword() == null || anonymous.getAnonymousNickname() == null) {
      throw new BusinessException(ReplyErrorCode.INVALID_ANONYMOUS_INPUT);
    }
    if (anonymous.getAnonymousPassword().isEmpty() || anonymous.getAnonymousNickname().isEmpty()) {
      throw new BusinessException(ReplyErrorCode.INVALID_ANONYMOUS_INPUT);
    }
  }


  public void updateParent(Reply parent) {
    if (parent == null) {
      return;
    }
    validateOccurCycle(parent);
    this.parent = parent;
    parent.getChild().add(this);
  }

  private void validateOccurCycle(Reply toBeParent) {
    if (dfs(this, toBeParent)) {
      throw new BusinessException(MenuErrorCode.OCCUR_CYCLE);
    }
  }

  private boolean dfs(Reply now, Reply notTobeChild) {
    if (now == notTobeChild) {
      return true;
    }
    for (Reply child : now.getChild()) {
      return dfs(child, notTobeChild);
    }
    return false;
  }

  public void delete() {
    this.isDelete = ReplyIsDelete.DELETED;
  }

  public void updateIsSecret(ReplyIsSecret isSecret) {
    this.isSecret = isSecret;
  }

  public void updateText(String text) {
    this.text = text;
  }

  public void validateReadable(Set<String> authorities) {
    if (authorities.contains(MANAGE_AUTHORITY)) {
      return;
    }

    if (this.isDelete == ReplyIsDelete.DELETED) {
      throw new BusinessException(ReplyErrorCode.ALREADY_DELETED);
    }
    post.validateReadable();
    post.validateBoardAccessAuthority(authorities);
  }

  public void updateLastModifiedIp(String currentClientIP) {
    this.lastModifiedIp = lastModifiedIp;
  }

  public static boolean hasManageAuthority(Set<String> authorities) {
    return authorities.contains(MANAGE_AUTHORITY);
  }

  public boolean hasAccessAuthority(Long accountId) {
    return accountId.equals(this.post.getAccount().getAccountId()) || accountId.equals(this.account.getAccountId());
  }
}
