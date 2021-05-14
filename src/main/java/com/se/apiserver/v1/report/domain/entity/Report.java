package com.se.apiserver.v1.report.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reportId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "post_id", referencedColumnName = "postId")
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "reply_id", referencedColumnName = "replyId")
  private Reply reply;

  @Column(length = 255, nullable = false)
  @Size(min = 2, max = 255)
  private String text;

  @Column(length = 20, nullable = false)
  @Size(min = 2, max = 20)
  @Enumerated(EnumType.STRING)
  private ReportStatus status;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "processor", referencedColumnName = "accountId")
  private Account processor;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "reporter", referencedColumnName = "accountId")
  private Account reporter;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "reported", referencedColumnName = "accountId", nullable = false)
  private Account reported;

  public Report(Post post, Reply reply, @Size(min = 2, max = 255) String text,
      @Size(min = 2, max = 20) ReportStatus status, Account processor,
      Account reporter, Account reported) {
    this.post = post;
    this.reply = reply;
    this.text = text;
    this.status = status;
    this.processor = processor;
    this.reporter = reporter;
    this.reported = reported;
  }

  public Report(Post post, Reply reply, @Size(min = 2, max = 255) String text,
      Account reporter, Account reported) {
    this(post, reply, text, ReportStatus.SUBMITTED, null, reporter, reported);
  }
}
