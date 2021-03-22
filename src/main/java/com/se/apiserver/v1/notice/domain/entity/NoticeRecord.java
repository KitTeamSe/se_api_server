package com.se.apiserver.v1.notice.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeRecord extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long noteceRecordId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "tag_id", referencedColumnName = "tagId")
  private Tag tag;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "post_id", referencedColumnName = "postId")
  private Post post;

  @Column(length = 255, nullable = false)
  @Size(min = 2, max = 255)
  private String text;

  @Builder
  public NoticeRecord(Long noteceRecordId, Tag tag, Post post, @Size(min = 2, max = 255) String text) {
    this.noteceRecordId = noteceRecordId;
    this.tag = tag;
    this.post = post;
    this.text = text;
  }
}
