package com.se.apiserver.v1.noticerecord.domain.service;

import com.se.apiserver.v1.account.application.service.AccountReadService;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.notice.domain.service.NoticeReadService;
import com.se.apiserver.v1.notice.infra.repository.NoticeJpaRepository;
import com.se.apiserver.v1.noticerecord.domain.entity.NoticeRecord;
import com.se.apiserver.v1.noticerecord.domain.error.NoticeRecordErrorCode;
import com.se.apiserver.v1.noticerecord.infra.dto.NoticeRecordReadDto;
import com.se.apiserver.v1.noticerecord.infra.repository.NoticeRecordJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeRecordReadService {

    private final NoticeRecordJpaRepository noticeRecordJpaRepository;
    private final AccountJpaRepository accountJpaRepository;
    private  final NoticeJpaRepository noticeJpaRepository;

    public PageImpl readAll(Pageable pageable) {
        Page<NoticeRecord> noticeRecords = noticeRecordJpaRepository.findAll(pageable);
        List<NoticeRecordReadDto.Response> responseList = noticeRecords.stream()
                .map(n -> NoticeRecordReadDto.Response.fromEntity(n))
                .collect(Collectors.toList());
        return new PageImpl(responseList, noticeRecords.getPageable(), noticeRecords.getTotalElements());
    }

    public NoticeRecordReadDto.Response readById(Long id) {
        NoticeRecord noticeRecord = noticeRecordJpaRepository.findById(id).orElseThrow(() -> new BusinessException(NoticeRecordErrorCode.NO_SUCH_NOTICERECORD));
        return NoticeRecordReadDto.Response.fromEntity(noticeRecord);
    }
    
    public List<NoticeRecordReadDto.Response> readByAccountId(Long accountId) {
        //Account 존재 확인
        accountJpaRepository.findById(accountId).orElseThrow(() -> new BusinessException(NoticeRecordErrorCode.NO_SUCH_ACCOUNT));
        //Notice 조회
        List<NoticeRecord> notices = noticeRecordJpaRepository.findNoticeRecordsByAccount_AccountId(accountId);

        List<NoticeRecordReadDto.Response> responses = notices.stream()
                .map(n -> NoticeRecordReadDto.Response.fromEntity(n))
                .collect(Collectors.toList());
        return responses;

    }

    public List<NoticeRecordReadDto.Response> readByNoticeId(Long noticeId) {
        //Notice 존재 확인
        noticeJpaRepository.findById(noticeId).orElseThrow(() -> new BusinessException(NoticeRecordErrorCode.NO_SUCH_NOTICE));
        //Notice 조회
        List<NoticeRecord> notices = noticeRecordJpaRepository.findNoticeRecordsByNotice_NoticeId(noticeId);

        List<NoticeRecordReadDto.Response> responses = notices.stream()
                .map(n -> NoticeRecordReadDto.Response.fromEntity(n))
                .collect(Collectors.toList());
        return responses;
    }

}
