package com.se.apiserver.v1.notice.application.service;

import com.se.apiserver.v1.account.domain.entity.*;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.AccountReceiveTagMappingJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.board.application.service.BoardCreateService;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.notice.domain.service.NoticeSendService;
import com.se.apiserver.v1.post.application.service.PostCreateService;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.infra.dto.PostCreateDto;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
public class NoticeSendServiceTest {

    @Autowired
    NoticeSendService noticeSendService;

    @Autowired
    TagJpaRepository tagJpaRepository;

    @Autowired
    AccountReceiveTagMappingJpaRepository accountReceiveTagMappingJpaRepository;

    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Autowired
    AccountJpaRepository accountJpaRepository;

    @Autowired
    BoardJpaRepository boardJpaRepository;

    @Autowired
    AttachJpaRepository attachJpaRepository;

    @Autowired
    PostCreateService postCreateService;

    Account account1;
    Account account2;
    Tag tag;
    Tag tag2;
    Board board;
    Attach attach;
    Long postId;

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

        board = new Board("freeboard", "자유게시판");
        boardJpaRepository.save(board);
        
        tag = new Tag("태그1");
        tagJpaRepository.save(tag);
        tag2 = new Tag("태그2");
        tagJpaRepository.save(tag2);

        attach = new Attach("testurl", "testfile");
        attachJpaRepository.save(attach);

        AccountReceiveTagMapping accountReceiveTagMapping1 = AccountReceiveTagMapping.builder()
                .account(account1)
                .tag(tag)
                .build();

        AccountReceiveTagMapping accountReceiveTagMapping2 = AccountReceiveTagMapping.builder()
                .account(account2)
                .tag(tag2)
                .build();

        accountReceiveTagMappingJpaRepository.save(accountReceiveTagMapping1);
        accountReceiveTagMappingJpaRepository.save(accountReceiveTagMapping2);

    }

    @Test
    void 게시글_알림_성공() {
        //given
        initData();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(account1.getAccountId(),
                "1", Arrays.asList(new SimpleGrantedAuthority("freeboard"))));

        //when
        postId = postCreateService.create(PostCreateDto.Request.builder()
                .boardId(board.getBoardId())
                .isNotice(PostIsNotice.NORMAL)
                .isSecret(PostIsSecret.SECRET)
                .postContent(new PostContent("제목...", "내용..."))
                .attachmentList(Arrays.asList(PostCreateDto.AttachDto.builder()
                        .attachId(attach.getAttachId())
                        .build()))
                .tagList(Arrays.asList(PostCreateDto.TagDto.builder()
                        .tagId(tag2.getTagId())
                        .build()))
                .build());
        //then

    }

    @Test
    void 사용자리스트_생성_성공() {
        //given
        initData();
        //when
        //then
        List<Long> tagIdList = new ArrayList<>();
        tagIdList.add(tag.getTagId());
        Assertions.assertThat(noticeSendService.createAccountList(tagIdList).size()).isEqualTo(1);
        Assertions.assertThat(noticeSendService.createAccountList(tagIdList).get(0)).isEqualTo(account1.getAccountId());
    }
}
