package com.se.apiserver.v1.noticerecord.infra.repository;

import com.se.apiserver.v1.notice.domain.entity.Notice;
import com.se.apiserver.v1.noticerecord.domain.entity.NoticeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface NoticeRecordJpaRepository extends JpaRepository<NoticeRecord, Long> {

    List<NoticeRecord> findNoticeRecordsByAccount_AccountId(Long accountId);
    List<NoticeRecord> findNoticeRecordsByNotice_NoticeId(Long noticeId);

}
