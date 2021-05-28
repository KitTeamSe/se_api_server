package com.se.apiserver.v1.notice.application.service;

import com.se.apiserver.v1.notice.domain.entity.Notice;
import com.se.apiserver.v1.notice.domain.service.NoticeCreateService;
import com.se.apiserver.v1.notice.infra.dto.NoticeCreateDto;
import com.se.apiserver.v1.notice.infra.repository.NoticeJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class NoticeCreateServiceTest {

    @Autowired
    NoticeJpaRepository noticeJpaRepository;

    @Autowired
    NoticeCreateService noticeCreateService;

    @Test
    void 알림_생성_성공() {
        //given
        //when
        Long id = noticeCreateService.save(NoticeCreateDto.Request.builder()
        .title("게시글 알림입니다.")
        .message("4학년 창의융합프로젝트2")
        .url("url")
        .build());

        //then
        Notice notice = noticeJpaRepository.findById(id).get();
        Assertions.assertThat(notice.getTitle()).isEqualTo("게시글 알림입니다.");
        Assertions.assertThat(notice.getMessage()).isEqualTo("4학년 창의융합프로젝트2");
        Assertions.assertThat(notice.getUrl()).isEqualTo("url");
    }
}
