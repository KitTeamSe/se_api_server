package com.se.apiserver.v1.attach.domain.usecase;


import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.infra.dto.AttachReadDto;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import com.se.apiserver.v1.post.domain.entity.*;
import com.se.apiserver.v1.post.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsDelete;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
@Transactional
class AttachReadUseCaseTest {

    @Autowired
    AttachJpaRepository attachJpaRepository;
    @Autowired
    AttachReadUseCase attachReadUseCase;
    @Autowired
    AttachDeleteUseCase attachDeleteUseCase;
    @Autowired
    PostJpaRepository postJpaRepository;
    @Autowired
    ReplyJpaRepository replyJpaRepository;
    @Autowired
    BoardJpaRepository boardJpaRepository;
    @Autowired
    MenuJpaRepository menuJpaRepository;
    @Autowired
    AuthorityJpaRepository authorityJpaRepository;

    Post post;
    Account account;
    Reply reply;
    Board board;
    Attach attachNoParent;
    Attach attachPost;
    Attach attachReply;

    private void initData() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(1,
                "1", Arrays.asList(new SimpleGrantedAuthority("freeboard"))));

        board = new Board("freeboard", "자유게시판");
        boardJpaRepository.save(board);


        post = new Post(new Anonymous("익명1", "testtest"), board,
                new PostContent("test.....", "title..."), PostIsNotice.NORMAL, PostIsSecret.NORMAL,
                new HashSet<>(Arrays.asList("freeboard")) ,
                new ArrayList<>(), new ArrayList<>());

        postJpaRepository.save(post);
        reply = Reply.builder()
                .account(account)
                .anonymous(
                        Anonymous.builder()
                                .anonymousNickname("익명1")
                                .anonymousPassword("testest")
                                .build()
                )
                .post(post)
                .status(ReplyIsDelete.NORMAL)
                .text("tetete")
                .build();
        replyJpaRepository.save(reply);

        attachNoParent = new Attach("testurl", "testfilename");
        attachPost = new Attach("testurl", "testfilename", post);
        attachReply = new Attach("testurl", "testfilename", reply);
        attachJpaRepository.save(attachNoParent);
        attachJpaRepository.save(attachPost);
        attachJpaRepository.save(attachReply);
    }

    @Test
    void 첨부파일_조회_성공() {
        //given
        initData();
        //when
        AttachReadDto.Response attachNoParentRes = attachReadUseCase.read(attachNoParent.getAttachId());
        AttachReadDto.Response attachPostRes = attachReadUseCase.read(attachPost.getAttachId());
        AttachReadDto.Response attachReplyRes = attachReadUseCase.read(attachReply.getAttachId());
        //then
        Assertions.assertThat(attachNoParentRes.getFileName()).isEqualTo(attachNoParent.getFileName());
        Assertions.assertThat(attachNoParentRes.getDownloadUrl()).isEqualTo(attachNoParent.getDownloadUrl());
        Assertions.assertThat(attachNoParentRes.getReplyId()).isEqualTo(null);
        Assertions.assertThat(attachNoParentRes.getPostId()).isEqualTo(null);

        Assertions.assertThat(attachPostRes.getFileName()).isEqualTo(attachPost.getFileName());
        Assertions.assertThat(attachPostRes.getDownloadUrl()).isEqualTo(attachPost.getDownloadUrl());
        Assertions.assertThat(attachPostRes.getReplyId()).isEqualTo(null);
        Assertions.assertThat(attachPostRes.getPostId()).isEqualTo(post.getPostId());

        Assertions.assertThat(attachReplyRes.getFileName()).isEqualTo(attachReply.getFileName());
        Assertions.assertThat(attachReplyRes.getDownloadUrl()).isEqualTo(attachReply.getDownloadUrl());
        Assertions.assertThat(attachReplyRes.getReplyId()).isEqualTo(reply.getReplyId());
        Assertions.assertThat(attachReplyRes.getPostId()).isEqualTo(null);
    }

    @Test
    void 첨부파일_전체_조회_성공() {
        //given
        initData();
        //when
        PageImpl page = attachReadUseCase.readAllByPage(new PageRequest(1, 10, Sort.Direction.ASC).of());
        //then
        Assertions.assertThat(page.getTotalElements()).isEqualTo(3);
    }

    @Test
    void 게시글별_첨부파일_조회_성공() {
        //given
        initData();
        //when
        List<AttachReadDto.Response> responses = attachReadUseCase.readAllByPostId(post.getPostId());
        //then
        Assertions.assertThat(responses.size()).isEqualTo(1);
    }

    @Test
    void 댓글별_첨부파일_조회_성공() {
        //given
        initData();
        //when
        List<AttachReadDto.Response> responses = attachReadUseCase.readAllByReplyId(post.getPostId());
        //then
        Assertions.assertThat(responses.size()).isEqualTo(1);
    }
}