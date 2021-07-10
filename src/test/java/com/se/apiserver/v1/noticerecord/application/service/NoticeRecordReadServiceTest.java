package com.se.apiserver.v1.noticerecord.application.service;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.notice.application.service.NoticeCreateService;
import com.se.apiserver.v1.notice.application.dto.NoticeCreateDto;
import com.se.apiserver.v1.noticerecord.application.error.NoticeRecordErrorCode;
import com.se.apiserver.v1.noticerecord.application.dto.NoticeRecordCreateDto;
import com.se.apiserver.v1.noticerecord.infra.repository.NoticeRecordJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class NoticeRecordReadServiceTest {

    @Autowired
    NoticeRecordReadService noticeRecordReadService;

    @Autowired
    NoticeRecordJpaRepository noticeRecordJpaRepository;
    
    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Autowired
    AccountJpaRepository accountJpaRepository;

    @Autowired
    NoticeCreateService noticeCreateService;

    @Autowired
    NoticeRecordCreateService noticeRecordCreateService;

    Account account1;
    Account account2;
    Long noticeId;
    Long accountId1;
    Long accountId2;
    Long noticeRecordid1;
    Long noticeRecordid2;

    void initData() {
        Question question = Question.builder().text("질문1").build();
        questionJpaRepository.save(question);

        account1 = Account.builder()
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

        account2 = Account.builder()
                .idString("test2")
                .email("test2@test.com")
                .informationOpenAgree(InformationOpenAgree.AGREE)
                .name("test2")
                .nickname("test2")
                .password("dasdasdasd2")
                .phoneNumber("555555555552")
                .studentId("20003152")
                .type(AccountType.STUDENT)
                .question(question)
                .answer("dasdasd2")
                .build();
        
        accountJpaRepository.save(account1);
        accountJpaRepository.save(account2);

        noticeId = noticeCreateService.save(NoticeCreateDto.Request.builder()
                .title("게시글 알림입니다.")
                .message("4학년 창의융합프로젝트2")
                .url("url")
                .build());
        

        accountId1 = accountJpaRepository.findByIdString("test").get().getAccountId();
        accountId2 = accountJpaRepository.findByIdString("test2").get().getAccountId();

        noticeRecordid1 = noticeRecordCreateService.create(NoticeRecordCreateDto.Request.builder()
                .accountId(accountId1)
                .noticeId(noticeId)
                .build());

        noticeRecordid2 = noticeRecordCreateService.create(NoticeRecordCreateDto.Request.builder()
                .accountId(accountId2)
                .noticeId(noticeId)
                .build());
    }
    
    @Test
    void 알림내역_전체검색_성공(){
        //given
        initData();
        //when
        //then
        PageImpl responses = noticeRecordReadService.readAll(PageRequest.builder()
                .size(10)
                .direction(Sort.Direction.ASC)
                .page(1)
                .build().of());
        Assertions.assertThat(responses.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 알림내역_조회_성공() {
        //given
        initData();
        //when
        //then
        Assertions.assertThat(noticeRecordReadService.readById(noticeRecordid1).getAccountId()).isEqualTo(accountId1);
    }

    @Test
    void 알림내역_미존재_실패() {
        //given
        initData();
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            noticeRecordReadService.readById(100L);
        }).isInstanceOf(BusinessException.class).hasMessage(NoticeRecordErrorCode.NO_SUCH_NOTICERECORD.getMessage());
    }

    @Test
    void 알림내역_사용자로검색_성공() {
        //given
        initData();
        //when
        //then
        Assertions.assertThat(noticeRecordReadService.readByAccountId(accountId1)).size().isEqualTo(1);
        Assertions.assertThat(noticeRecordReadService.readByAccountId(accountId1).get(0).getAccountId()).isEqualTo(accountId1);
    }

    @Test
    void 알림내역_사용자미존재_실패() {
        //given
        initData();
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            noticeRecordReadService.readByAccountId(100L);
        }).isInstanceOf(BusinessException.class).hasMessage(NoticeRecordErrorCode.NO_SUCH_ACCOUNT.getMessage());
    }

    @Test
    void 알림내역_알림으로검색_성공() {
        //given
        initData();
        //when
        //then
        Assertions.assertThat(noticeRecordReadService.readByNoticeId(noticeId)).size().isEqualTo(2);
        Assertions.assertThat(noticeRecordReadService.readByNoticeId(noticeId).get(0).getTitle()).isEqualTo("게시글 알림입니다.");
    }

    @Test
    void 알림내역_알림미존재_실패() {
        //given
        initData();
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            noticeRecordReadService.readByNoticeId(100L);
        }).isInstanceOf(BusinessException.class).hasMessage(NoticeRecordErrorCode.NO_SUCH_NOTICE.getMessage());

    }
}
