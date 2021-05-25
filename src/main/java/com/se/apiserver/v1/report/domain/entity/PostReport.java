package com.se.apiserver.v1.report.domain.entity;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.post.domain.entity.Post;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(ReportType.Values.POST)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class PostReport extends Report {

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "post_id", referencedColumnName = "postId", nullable = false)
  private Post post;

  public PostReport(Post post, String description, Account reporter) {
    super(description, reporter);
    this.post = post;
  }

  @Override
  public Long getTargetId() {
    return post.getPostId();
  }
}
