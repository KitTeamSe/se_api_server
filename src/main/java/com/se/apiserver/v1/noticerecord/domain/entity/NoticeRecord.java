package com.se.apiserver.v1.noticerecord.domain.entity;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.notice.domain.entity.Notice;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeRecordId;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "account_id", referencedColumnName = "accountId", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "notice_id", referencedColumnName = "noticeId", nullable = false)
    private Notice notice;

    @Builder
    public NoticeRecord(Long noticeRecordId, Account account, Notice notice) {
        this.noticeRecordId = noticeRecordId;
        this.account = account;
        this.notice = notice;
    }
}
