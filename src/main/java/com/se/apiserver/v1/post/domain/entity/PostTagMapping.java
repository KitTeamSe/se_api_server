package com.se.apiserver.v1.post.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTagMapping extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long postTagMappingId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "post_id", referencedColumnName = "postId", nullable = false)
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "tag_id", referencedColumnName = "tagId", nullable = false)
  private Tag tag;

  @Builder
  public PostTagMapping(Long postTagMappingId, Post post, Tag tag) {
    this.postTagMappingId = postTagMappingId;
    this.post = post;
    this.tag = tag;
  }

  public void setPost(Post post) {
    this.post = post;
    post.addTag(this);
  }
}
