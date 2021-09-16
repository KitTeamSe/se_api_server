package com.se.apiserver.v1.post.domain.entity;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

  public static final String MANAGE_AUTHORITY = "MENU_MANAGE";
  public static final Integer MAX_TAG_CAPACITY = 20;
  public static final Integer MAX_ATTACH_CAPACITY = 10;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long postId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "board_id", referencedColumnName = "boardId")
  private Board board;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "account_id", referencedColumnName = "accountId")
  private Account account;

  @Embedded
  private PostContent postContent;

  @Embedded
  private Anonymous anonymous;

  @Column(length = 10, nullable = false)
  @Enumerated(EnumType.STRING)
  private PostIsNotice isNotice;

  @Column(length = 10, nullable = false)
  @Enumerated(EnumType.STRING)
  private PostIsSecret isSecret;

  @Column(length = 10, nullable = false)
  @Enumerated(EnumType.STRING)
  private PostIsDeleted postIsDeleted;

  @Column(nullable = false)
  private Integer views;

  @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<Reply> replies = new HashSet<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<Attach> attaches = new HashSet<>();

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
  @JoinTable(name = "post_tag"
      , joinColumns = @JoinColumn(name = "post_id")
      , inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<Tag> tags = new HashSet<>();

  @Column(nullable = false, updatable = false)
  private String createdIp;

  @Column
  private String lastModifiedIp;

  // 첨부 파일, 태그 모두 존재 입력
  public Post(Board board, PostContent postContent, PostIsNotice isNotice,
      PostIsSecret isSecret, Set<String> authorities,
      List<Tag> tags, List<Attach> attaches, String createdIp) {
    validateBoardAccessAuthority(board, authorities);
    this.board = board;
    this.postContent = postContent;
    updateNotice(isNotice, authorities);
    this.isSecret = isSecret;
    this.postIsDeleted = PostIsDeleted.NORMAL;
    this.views = 0;
    this.createdIp = createdIp;
    addAttaches(attaches);
    addTags(tags);
  }

  public Post(Account account, Board board, PostContent postContent,
      PostIsNotice isNotice, PostIsSecret isSecret, Set<String> authorities
      , List<Tag> tags, List<Attach> attaches, String createdIp) {
    this(board, postContent, isNotice, isSecret, authorities, tags, attaches, createdIp);
    this.account = account;
  }

  public Post(Anonymous anonymous, Board board, PostContent postContent,
      PostIsNotice isNotice, PostIsSecret isSecret, Set<String> authorities
      , List<Tag> tags, List<Attach> attaches, String createdIp) {
    this(board, postContent, isNotice, isSecret, authorities, tags, attaches, createdIp);
    this.anonymous = anonymous;
  }

  private void updateNotice(PostIsNotice isNotice, Set<String> authorities) {
    validateNoticeAccess(isNotice, authorities);
    this.isNotice = isNotice;
  }

  public void updateBoard(Board board, Set<String> authorities) {
    hasManageAuthority(authorities);
    this.board = board;
  }

  public void validateNoticeAccess(PostIsNotice isNotice, Set<String> authorities) {
    if (isNotice == PostIsNotice.NOTICE && !hasManageAuthority(authorities)) {
      throw new BusinessException(PostErrorCode.ONLY_ADMIN_SET_NOTICE);
    }
  }

  public boolean hasManageAuthority(Set<String> authorities) {
    return authorities.contains(MANAGE_AUTHORITY);
  }

  public void validateAccountAccess(Long contextAccountId, Set<String> authorities) {
    if (!account.getAccountId().equals(contextAccountId) && !hasManageAuthority(authorities)) {
      throw new BusinessException(AccountErrorCode.CAN_NOT_ACCESS_ACCOUNT);
    }
  }

  public void validateAccountAccess(Set<String> authorities) {
    if (!hasManageAuthority(authorities)) {
      throw new BusinessException(AccountErrorCode.CAN_NOT_ACCESS_ACCOUNT);
    }
  }


  public void validateBoardAccessAuthority(Board board, Set<String> authorities) {
    board.validateAccessAuthority(authorities);
  }

  public void validateBoardAccessAuthority(Set<String> authorities) {
    validateBoardAccessAuthority(this.board, authorities);

  }

  public void validateBoardManageAuthority(Board board, Set<String> authorities) {
    board.validateManageAuthority(authorities);
  }

  public void addAttaches(List<Attach> attachList) {
    if (attachList == null) {
      return;
    }
    attachList.stream()
        .forEach(a -> a.updatePost(this));
    if (this.attaches.size() > MAX_ATTACH_CAPACITY) {
      throw new BusinessException(PostErrorCode.OVER_MAX_ATTACH_CAPACITY);
    }
  }

  public void addAttache(Attach attach) {
    this.attaches.add(attach);
  }

  public void addTags(List<Tag> tags) {
    this.tags.addAll(tags);
    if (this.tags.size() > MAX_TAG_CAPACITY) {
      throw new BusinessException(PostErrorCode.OVER_MAX_TAG_CAPACITY);
    }
  }

  public void addTag(Tag Tag) {
    this.tags.add(Tag);
  }

  public void updateContent(PostContent postContent) {
    this.postContent = postContent;
  }


  public void updateIsNotice(PostIsNotice isNotice, Set<String> authorities) {
    validateNoticeAccess(isNotice, authorities);
    this.isNotice = isNotice;
  }

  public void updateIsSecret(PostIsSecret isSecret) {
    this.isSecret = isSecret;
  }

  public void updateAttaches(List<Attach> attachList) {
    this.attaches.forEach(a -> {
      if (!attachList.contains(a)) {
        a.setPost(null);
      }
    });

    attachList.forEach(a -> {
      if (!this.attaches.contains(a)) {
        a.setPost(this);
        addAttache(a);
      }
    });
  }

  public void updateTags(List<Tag> tags) {
    this.tags.clear();
    addTags(tags);
  }

  public void update(Board board, PostContent postContent, PostIsNotice isNotice,
      PostIsSecret isSecret, Set<String> authorities, String ip) {
    updateBoard(board, authorities);
    updateContent(postContent);
    updateIsNotice(isNotice, authorities);
    updateIsSecret(isSecret);
    updateLastModifiedIp(ip);
  }

  private void updateLastModifiedIp(String ip) {
    this.lastModifiedIp = ip;
  }

  public boolean isOwner(Account contextAccount) {
    if (this.account == contextAccount) {
      return true;
    }
    return false;
  }

  public boolean isOwner(Long accountId) {
    if (this.account.getAccountId() == accountId) {
      return true;
    }
    return false;
  }

  public String getAnonymousPassword() {
    if (this.anonymous == null) {
      throw new BusinessException(PostErrorCode.NOT_ANONYMOUS_POST);
    }
    return anonymous.getAnonymousPassword();
  }

  public void delete(Account contextAccount, Set<String> authorities) {
    validateAccountAccess(contextAccount.getAccountId(), authorities);
    delete();
  }

  public void delete(Set<String> authorities) {
    validateAccountAccess(authorities);
    delete();
    this.board = null;
  }

  public void delete() {
    if (this.postIsDeleted == PostIsDeleted.DELETED) {
      throw new BusinessException(PostErrorCode.DELETED_POST);
    }

    this.postIsDeleted = PostIsDeleted.DELETED;
  }

  public void increaseViews() {
    views += 1;
  }

  public void validateReadable() {
    if (this.postIsDeleted == PostIsDeleted.DELETED) {
      throw new BusinessException(PostErrorCode.DELETED_POST);
    }
  }

  public void addReply(Reply reply) {
    this.replies.add(reply);
  }
}
