package com.se.apiserver.v1.noticerecord.domain.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.noticerecord.domain.entity.NoticeRecord;
import com.se.apiserver.v1.noticerecord.domain.error.NoticeRecordErrorCode;
import com.se.apiserver.v1.noticerecord.infra.repository.NoticeRecordJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeRecordDeleteService {

    private  final NoticeRecordJpaRepository noticeRecordJpaRepository;

    @Transactional
    public boolean delete(Long id) {
        NoticeRecord noticeRecord = noticeRecordJpaRepository.findById(id).orElseThrow(() -> new BusinessException(NoticeRecordErrorCode.NO_SUCH_NOTICERECORD));
        noticeRecordJpaRepository.delete(noticeRecord);
        return true;
    }
}
