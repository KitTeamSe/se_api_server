package com.se.apiserver.v1.report.domain.entity;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(value = ReportType.Values.REPLY)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class ReplyReport extends Report {

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "reply_id", referencedColumnName = "replyId", nullable = false)
  private Reply reply;

  public ReplyReport(Reply reply, String description, Account reporter) {
    super(description, reporter);
    this.reply = reply;
  }

  @Override
  public Long getTargetId() {
    return reply.getReplyId();
  }
}
