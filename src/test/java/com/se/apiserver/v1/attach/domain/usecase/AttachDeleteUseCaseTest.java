package com.se.apiserver.v1.attach.domain.usecase;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.domain.error.AttachErrorCode;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AttachDeleteUseCaseTest {
    @Autowired
    AttachJpaRepository attachJpaRepository;
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
    Menu menu;
    Board board;
    Attach attachNoParent;
    Attach attachPost;
    Attach attachReply;
    Authority authority;

    private void initData() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(1,
                "1", Arrays.asList(new SimpleGrantedAuthority("BOARD_freeeboard_ACCESS"))));
        authority = Authority.builder()
                .nameEng("BOARD_freeeboard_ACCESS")
                .nameKor("자유게시판 접근")
                .build();
        authorityJpaRepository.save(authority);

        menu = Menu.builder()
                .url("testurl")
                .nameEng("testname")
                .nameKor("테스트이름")
                .description("테스트 설명")
                .menuType(MenuType.BOARD)
                .menuOrder(1)
                .build();
        menu.updateAuthority(authority);
        menuJpaRepository.save(menu);

        board = Board.builder()
                .menu(menu)
                .nameEng("testname")
                .nameKor("테스트이름")
                .build();
        boardJpaRepository.save(board);


        post = new Post(new Anonymous("익명1", "testtest"), board,
                new PostContent("test.....", "title..."), PostIsNotice.NORMAL, PostIsSecret.NORMAL,
                new HashSet<>(Arrays.asList("BOARD_freeeboard_ACCESS")) ,
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

        attachNoParent = Attach.builder()
                .downloadUrl("testurl")
                .fileName("testfilename")
                .build();

        attachPost = Attach.builder()
                .downloadUrl("testurl")
                .fileName("testfilename")
                .build();
        attachPost.updatePost(post);

        attachReply = Attach.builder()
                .downloadUrl("testurl")
                .fileName("testfilename")
                .build();
        attachReply.updateReply(reply);

        attachJpaRepository.save(attachNoParent);
        attachJpaRepository.save(attachPost);
        attachJpaRepository.save(attachReply);
    }

    @Test
    void 첨부파일_삭제_성공() {
        //given
        initData();
        //when
        Long attachNoParentId = attachNoParent.getAttachId();
        Long attachPostId = attachPost.getAttachId();
        Long attachReplyId = attachReply.getAttachId();
        attachDeleteUseCase.delete(attachNoParent.getAttachId());
        attachDeleteUseCase.delete(attachPost.getAttachId());
        attachDeleteUseCase.delete(attachReply.getAttachId());
        //then
        Assertions.assertThat(post.getAttaches().size()).isEqualTo(0);
        Assertions.assertThat(reply.getAttaches().size()).isEqualTo(0);
        Assertions.assertThat(attachJpaRepository.findById(attachNoParentId).isEmpty()).isEqualTo(true);
        Assertions.assertThat(attachJpaRepository.findById(attachPostId).isEmpty()).isEqualTo(true);
        Assertions.assertThat(attachJpaRepository.findById(attachReplyId).isEmpty()).isEqualTo(true);
    }

    @Test
    void 첨부파일_미존재_실패() {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            attachDeleteUseCase.delete(4444L);
        }).isInstanceOf(BusinessException.class).hasMessage(AttachErrorCode.NO_SUCH_ATTACH.getMessage());

    }
}