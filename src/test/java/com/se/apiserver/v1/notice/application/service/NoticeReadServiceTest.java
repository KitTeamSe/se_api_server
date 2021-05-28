package com.se.apiserver.v1.notice.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.notice.domain.error.NoticeErrorCode;
import com.se.apiserver.v1.notice.domain.service.NoticeCreateService;
import com.se.apiserver.v1.notice.domain.service.NoticeReadService;
import com.se.apiserver.v1.notice.infra.dto.NoticeCreateDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class NoticeReadServiceTest {

    @Autowired
    NoticeReadService noticeReadService;

    @Autowired
    NoticeCreateService noticeCreateService;

    Long noticeId;
    Long noticeId1;

    void initData() {
        noticeId = noticeCreateService.save(NoticeCreateDto.Request.builder()
                .title("게시글 알림입니다.")
                .message("4학년 창의융합프로젝트2")
                .url("url")
                .build());

        noticeId1 = noticeCreateService.save(NoticeCreateDto.Request.builder()
                .title("댓글 알림입니다.")
                .message("4학년 창의융합프로젝트2")
                .url("url")
                .build());
    }

    @Test
    void 알림_전체검색_성공(){
        //given
        initData();
        //when
        //then
        PageImpl responses = noticeReadService.readAll(PageRequest.builder()
                .size(10)
                .direction(Sort.Direction.ASC)
                .page(1)
                .build().of());
        Assertions.assertThat(responses.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 알림_조회_성공() {
        //given
        initData();
        //when
        //then
        Assertions.assertThat(noticeReadService.readById(noticeId).getTitle()).isEqualTo("게시글 알림입니다.");
    }

    @Test
    void 알림_미존재_실패() {
        //given
        initData();
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            noticeReadService.readById(100L);
        }).isInstanceOf(BusinessException.class).hasMessage(NoticeErrorCode.NO_SUCH_NOTICE.getMessage());
    }
}
