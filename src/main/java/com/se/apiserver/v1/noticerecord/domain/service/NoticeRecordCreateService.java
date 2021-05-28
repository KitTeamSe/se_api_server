package com.se.apiserver.v1.noticerecord.domain.service;


import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.notice.domain.entity.Notice;
import com.se.apiserver.v1.notice.infra.repository.NoticeJpaRepository;
import com.se.apiserver.v1.noticerecord.domain.entity.NoticeRecord;
import com.se.apiserver.v1.noticerecord.domain.error.NoticeRecordErrorCode;
import com.se.apiserver.v1.noticerecord.infra.dto.NoticeRecordCreateDto;
import com.se.apiserver.v1.noticerecord.infra.repository.NoticeRecordJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeRecordCreateService {
    private final NoticeRecordJpaRepository noticeRecordJpaRepository;
    private final AccountJpaRepository accountJpaRepository;
    private final NoticeJpaRepository noticeJpaRepository;

    public Long create(NoticeRecordCreateDto.Request request) {

        Account account = accountJpaRepository.findById(request.getAccountId()).orElseThrow(() -> new BusinessException(NoticeRecordErrorCode.NO_SUCH_ACCOUNT));
        Notice notice = noticeJpaRepository.findById(request.getNoticeId()).orElseThrow(() -> new BusinessException(NoticeRecordErrorCode.NO_SUCH_NOTICE));

        NoticeRecord noticeRecord = NoticeRecord.builder()
                .account(account)
                .notice(notice)
                .build();
        noticeRecordJpaRepository.save(noticeRecord);
        return noticeRecord.getNoticeRecordId();
    }
}
