package com.se.apiserver.v1.notice.application.service;

import com.se.apiserver.v1.account.domain.entity.*;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.post.application.dto.PostCreateDto;
import com.se.apiserver.v1.post.application.service.PostCreateService;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import com.se.apiserver.v1.taglistening.domain.entity.TagListening;
import com.se.apiserver.v1.taglistening.infra.repository.TagListeningJpaRepository;
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
    TagListeningJpaRepository tagListeningJpaRepository;

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

        tag = new Tag("tag1");
        tagJpaRepository.save(tag);
        tag2 = new Tag("tag2");
        tagJpaRepository.save(tag2);

        attach = new Attach("testurl", "testfile");
        attachJpaRepository.save(attach);

        TagListening TagListening1 = new TagListening(account1, tag);
        TagListening TagListening2 = new TagListening(account2, tag2);

        tagListeningJpaRepository.save(TagListening1);
        tagListeningJpaRepository.save(TagListening2);

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
        Assertions.assertThat(noticeSendService.getTagListeners(tagIdList).size()).isEqualTo(1);
        Assertions.assertThat(noticeSendService.getTagListeners(tagIdList).get(0)).isEqualTo(account1.getAccountId());
    }
}