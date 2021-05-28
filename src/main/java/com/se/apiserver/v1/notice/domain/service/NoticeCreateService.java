package com.se.apiserver.v1.notice.domain.service;

import com.se.apiserver.v1.notice.domain.entity.Notice;
import com.se.apiserver.v1.notice.infra.dto.NoticeCreateDto;
import com.se.apiserver.v1.notice.infra.repository.NoticeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeCreateService {
    private final NoticeJpaRepository noticeJpaRepository;

    public Long save (NoticeCreateDto.Request request) {
        Notice notice = new Notice(request.getTitle(), request.getMessage(), request.getUrl());
        noticeJpaRepository.save(notice);
        return notice.getNoticeId();
    }
}
