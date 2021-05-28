package com.se.apiserver.v1.notice.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.notice.domain.entity.Notice;
import com.se.apiserver.v1.notice.domain.error.NoticeErrorCode;
import com.se.apiserver.v1.notice.infra.dto.NoticeReadDto;
import com.se.apiserver.v1.notice.infra.repository.NoticeJpaRepository;
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
public class NoticeReadUseCase {

    private final NoticeJpaRepository noticeJpaRepository;

    public PageImpl readAll(Pageable pageable) {
        Page<Notice> notices = noticeJpaRepository.findAll(pageable);
        List<NoticeReadDto.ReadResponse> responseList = notices.stream()
                .map(n -> NoticeReadDto.ReadResponse.fromEntity(n))
                .collect(Collectors.toList());
        return new PageImpl(responseList, notices.getPageable(), notices.getTotalElements());
    }

    public NoticeReadDto.ReadResponse readById(Long id) {
        Notice notice = noticeJpaRepository.findById(id).orElseThrow(() -> new BusinessException(NoticeErrorCode.NO_SUCH_NOTICE));
        return NoticeReadDto.ReadResponse.fromEntity(notice);
    }

}
