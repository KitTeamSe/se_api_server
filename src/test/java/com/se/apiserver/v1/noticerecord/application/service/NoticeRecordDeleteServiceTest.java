package com.se.apiserver.v1.noticerecord.application.service;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.notice.domain.service.NoticeCreateService;
import com.se.apiserver.v1.notice.infra.dto.NoticeCreateDto;
import com.se.apiserver.v1.noticerecord.domain.entity.NoticeRecord;
import com.se.apiserver.v1.noticerecord.domain.error.NoticeRecordErrorCode;
import com.se.apiserver.v1.noticerecord.domain.service.NoticeRecordCreateService;
import com.se.apiserver.v1.noticerecord.domain.service.NoticeRecordDeleteService;
import com.se.apiserver.v1.noticerecord.infra.dto.NoticeRecordCreateDto;
import com.se.apiserver.v1.noticerecord.infra.repository.NoticeRecordJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class NoticeRecordDeleteServiceTest {

    @Autowired
    NoticeRecordJpaRepository noticeRecordJpaRepository;

    @Autowired
    NoticeRecordCreateService noticeRecordCreateService;

    @Autowired
    NoticeRecordDeleteService noticeRecordDeleteService;

    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Autowired
    AccountJpaRepository accountJpaRepository;

    @Autowired
    NoticeCreateService noticeCreateService;

    Account account;
    Long noticeId;
    Long accountId;
    Long id;

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

        id = noticeRecordCreateService.create(NoticeRecordCreateDto.Request.builder()
                .accountId(accountId)
                .noticeId(noticeId)
                .build());

    }

    @Test
    void 알림내역_삭제_성공() {
        //given
        initData();
        //when
        NoticeRecord noticeRecord = noticeRecordJpaRepository.findById(id).get();
        Long noticeRecordId = noticeRecord.getNoticeRecordId();
        noticeRecordDeleteService.delete(id);
        //then
        Assertions.assertThat(noticeRecordJpaRepository.findById(noticeRecordId).isEmpty()).isEqualTo(true);
    }

    @Test
    void 알림내역_미존재_실패(){
        //given
        initData();
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            noticeRecordDeleteService.delete(100L);
        }).isInstanceOf(BusinessException.class).hasMessage(NoticeRecordErrorCode.NO_SUCH_NOTICERECORD.getMessage());
    }
}
