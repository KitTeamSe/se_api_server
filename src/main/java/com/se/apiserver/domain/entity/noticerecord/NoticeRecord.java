package com.se.apiserver.domain.entity.noticerecord;

import com.se.apiserver.domain.entity.BaseEntity;
import com.se.apiserver.domain.entity.fcm.Fcm;
import com.se.apiserver.domain.entity.post.Post;
import com.se.apiserver.domain.entity.tag.Tag;
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

  @ManyToOne
  @JoinColumn(name = "fcm_id", referencedColumnName = "fcmId", nullable = false)
  private Fcm fcm;

  @ManyToOne
  @JoinColumn(name = "tag_id", referencedColumnName = "tagId")
  private Tag tag;

  @ManyToOne
  @JoinColumn(name = "post_id", referencedColumnName = "postId")
  private Post post;

  @Column(length = 255, nullable = false)
  @Size(min = 2, max = 255)
  private String text;

  @Builder
  public NoticeRecord(Long noteceRecordId, Fcm fcm, Tag tag, Post post, @Size(min = 2, max = 255) String text) {
    this.noteceRecordId = noteceRecordId;
    this.fcm = fcm;
    this.tag = tag;
    this.post = post;
    this.text = text;
  }
}
