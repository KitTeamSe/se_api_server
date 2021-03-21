package com.se.apiserver.domain.entity.post;

import com.se.apiserver.domain.entity.tag.Tag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTagMapping {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long postTagMappingId;

  @ManyToOne
  @JoinColumn(name = "post_id", referencedColumnName = "postId", nullable = false)
  private Post post;

  @ManyToOne
  @JoinColumn(name = "tag_id", referencedColumnName = "tagId", nullable = false)
  private Tag tag;

  @Builder
  public PostTagMapping(Long postTagMappingId, Post post, Tag tag) {
    this.postTagMappingId = postTagMappingId;
    this.post = post;
    this.tag = tag;
  }
}
