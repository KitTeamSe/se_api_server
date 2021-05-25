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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {
  public static final String MANAGE_AUTHORITY = "MENU_MANAGE";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long postId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "board_id", referencedColumnName = "boardId", nullable = false)
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

  @Column(nullable = false)
  private Integer views;

  @Column(nullable = false)
  private Integer numReply;

  @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Reply> replies = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Attach> attaches = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<PostTagMapping> tags = new ArrayList<>();

  public Post(Board board, PostContent postContent, PostIsNotice isNotice,
              PostIsSecret isSecret, Set<String> authorities,
              List<PostTagMapping> tags ) {
    validateBoardAccessAuthority(board, authorities);
    this.board = board;
    this.postContent = postContent;
    updateNotice(isNotice, authorities);
    this.isSecret = isSecret;
    this.views = 0;
    this.numReply = 0;
    addAttaches(attaches);
    addTags(tags);
  }

  private void updateNotice(PostIsNotice isNotice, Set<String> authorities) {
    validateNoticeAccess(isNotice, authorities);
    this.isNotice = isNotice;
  }

  public void updateBoard(Board board, Set<String> authorities) {
    hasManageAuthority(authorities);
    this.board = board;
  }


  public Post(Account account, Board board,  PostContent postContent,
              PostIsNotice isNotice, PostIsSecret isSecret, Set<String> authorities
          , List<PostTagMapping> tags) {
    this(board, postContent, isNotice, isSecret, authorities, tags);
    validateBoardAccessAuthority(board, authorities);
    this.account = account;
  }

  public Post(Anonymous anonymous, Board board,  PostContent postContent,
              PostIsNotice isNotice, PostIsSecret isSecret, Set<String> authorities,
              List<PostTagMapping> tags) {
    this(board, postContent, isNotice, isSecret, authorities, tags);
    this.anonymous = anonymous;
  }

  public void validateNoticeAccess(PostIsNotice isNotice, Set<String> authorities) {
    if(isNotice == PostIsNotice.NOTICE && !hasManageAuthority(authorities))
      throw new BusinessException(PostErrorCode.ONLY_ADMIN_SET_NOTICE);
  }

  public boolean hasManageAuthority(Set<String> authorities) {
    return authorities.contains(MANAGE_AUTHORITY);
  }

  public void validateAccountAccess(Account contextAccount, Set<String> authorities) {
    if(!account.equals(contextAccount) && !hasManageAuthority(authorities))
      throw new BusinessException(AccountErrorCode.CAN_NOT_ACCESS_ACCOUNT);
  }


  public void validateBoardAccessAuthority(Board board, Set<String> authorities) {
    board.validateAccessAuthority(authorities);
  }

  public void addAttaches(List<Attach> attachList) {
    attachList.stream()
            .forEach(a -> a.updatePost(this));
  }

  public void addAttache(Attach attach) {
    this.attaches.add(attach);
  }

  public void addTags(List<PostTagMapping> postTagMappings) {
    postTagMappings.stream()
            .forEach(t -> t.setPost(this));
  }

  public void addTag(PostTagMapping postTagMapping) {
    this.tags.add(postTagMapping);
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
    attachList.stream()
            .forEach(a -> {
              if(!this.attaches.contains(a)) {
                addAttache(a);
              }
            });
  }

  public void updateTags(List<PostTagMapping> postTagMappings) {
    this.tags.forEach(t -> {
      t.setPost(null);
    });
    this.tags.clear();
    addTags(postTagMappings);
  }

  public void update(Board board, PostContent postContent, PostIsNotice isNotice,
                     PostIsSecret isSecret, List<Attach> attachList, List<PostTagMapping> tags, Set<String> authorities) {
    updateBoard(board, authorities);
    updateContent(postContent);
    updateIsNotice(isNotice, authorities);
    updateIsSecret(isSecret);
    updateAttaches(attachList);
    updateTags(tags);
  }

  public boolean isOwner(Account contextAccount) {
    if(this.account == contextAccount)
      return true;
    return false;
  }

  public String getAnonymousPassword() {
    if(this.anonymous == null)
      throw new BusinessException(PostErrorCode.NOT_ANONYMOUS_POST);
    return anonymous.getAnonymousPassword();
  }
}
