package com.se.apiserver.v1.noticerecord.application.service;

import com.se.apiserver.v1.account.application.service.AccountCreateService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.notice.application.service.NoticeCreateService;
import com.se.apiserver.v1.notice.application.dto.NoticeCreateDto;
import com.se.apiserver.v1.noticerecord.domain.entity.NoticeRecord;
import com.se.apiserver.v1.noticerecord.application.error.NoticeRecordErrorCode;
import com.se.apiserver.v1.noticerecord.application.dto.NoticeRecordCreateDto;
import com.se.apiserver.v1.noticerecord.infra.repository.NoticeRecordJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class NoticeRecordCreateServiceTest {

    @Autowired
    NoticeRecordJpaRepository noticeRecordJpaRepository;

    @Autowired
    NoticeRecordCreateService noticeRecordCreateService;

    @Autowired
    NoticeCreateService noticeCreateService;

    @Autowired
    AccountCreateService accountCreateService;

    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Autowired
    AccountJpaRepository accountJpaRepository;

    Account account;
    Long noticeId;
    Long accountId;

    void initData() {
        Question question = Question.builder().text("질문1").build();
        questionJpaRepository.save(question);

        account = Account.builder()
                .idString("test")
                .email("test@test.com")
                .informationOpenAgree(InformationOpenAgree.AGREE)
                .name("test")
                .nickname("test")
                .password("dasdasdasd")
                .phoneNumber("55555555555")
                .studentId("20003156")
                .type(AccountType.STUDENT)
                .question(question)
                .answer("dasdasd")
                .build();

        accountJpaRepository.save(account);

        noticeId = noticeCreateService.save(NoticeCreateDto.Request.builder()
                .title("게시글 알림입니다.")
                .message("4학년 창의융합프로젝트2")
                .url("url")
                .build());

        accountId = accountJpaRepository.findByIdString("test").get().getAccountId();
    }

    @Test
    void 알림내역_생성_성공(){
        //given
        initData();

        //when
        Long id = noticeRecordCreateService.create(NoticeRecordCreateDto.Request.builder()
        .accountId(accountId)
        .noticeId(noticeId)
        .build());

        //then
        NoticeRecord noticeRecord = noticeRecordJpaRepository.findById(id).get();
        Assertions.assertThat(noticeRecord.getAccount().getAccountId()).isEqualTo(accountId);
        Assertions.assertThat(noticeRecord.getNotice().getNoticeId()).isEqualTo(noticeId);
    }

    @Test
    void 알림내역_생성_알림입력값_실패() {
        //given
        initData();
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            noticeRecordCreateService.create(NoticeRecordCreateDto.Request.builder()
            .accountId(accountId)
            .noticeId(5L)
            .build());
        }).isInstanceOf(BusinessException.class).hasMessage(NoticeRecordErrorCode.NO_SUCH_NOTICE.getMessage());

    }

    @Test
    void 알림내역_생성_사용자입력값_실패() {
        //given
        initData();
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            noticeRecordCreateService.create(NoticeRecordCreateDto.Request.builder()
                    .accountId(5L)
                    .noticeId(noticeId)
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(NoticeRecordErrorCode.NO_SUCH_ACCOUNT.getMessage());

    }
}
