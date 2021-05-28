package com.se.apiserver.v1.notice.application.service;

import com.se.apiserver.v1.notice.domain.service.NoticeSendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class NoticeSendServiceTest {

    @Autowired
    NoticeSendService noticeSendService;

    @Test
    void 게시글_알림_성공() {

    }
}
