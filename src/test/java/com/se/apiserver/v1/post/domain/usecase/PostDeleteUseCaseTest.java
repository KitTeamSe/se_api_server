package com.se.apiserver.v1.post.domain.usecase;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.attach.domain.usecase.AttachCreateUseCase;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsDelete;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Transactional
class PostDeleteUseCaseTest {
    @Autowired
    PostDeleteUseCase postDeleteUseCase;
    @Autowired
    AttachCreateUseCase attachCreateUseCase;
    @Autowired
    PostJpaRepository postJpaRepository;
    @Autowired
    BoardJpaRepository boardJpaRepository;
    @Autowired
    MenuJpaRepository menuJpaRepository;
    @Autowired
    QuestionJpaRepository questionJpaRepository;
    @Autowired
    AuthorityJpaRepository authorityJpaRepository;

    Account user;
    Menu menu;
    Board board;
    Post postAccount;
    Post postAnonymous;

    private void initData() {
        Question question = Question.builder().text("질문1").build();
        questionJpaRepository.save(question);
        user = Account.builder()
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

        menu = new Menu("testname", "testurl", "테스트이름", 1, "테스트 설명", MenuType.BOARD);
        menuJpaRepository.save(menu);

        board = new Board("freeboard", "자유게시판");
        boardJpaRepository.save(board);


        postAnonymous = new Post(new Anonymous("익명1", "testtest"), board,
                new PostContent("test.....", "title..."), PostIsNotice.NORMAL, PostIsSecret.NORMAL,new HashSet<>(Arrays.asList("MENU_MANAGE")) ,
                new ArrayList<>(), new ArrayList<>());

        postAccount = new Post(user, board,
                new PostContent("test.....", "title..."), PostIsNotice.NORMAL, PostIsSecret.NORMAL,new HashSet<>(Arrays.asList("MENU_MANAGE")) ,
                new ArrayList<>(), new ArrayList<>());

        postJpaRepository.save(postAccount);
        postJpaRepository.save(postAnonymous);
    }

    @Test
    void 삭제_성공() {
        //given
        initData();
        //when
        Long postAccountId = postAccount.getPostId();
        Long postAnonymousId = postAnonymous.getPostId();
        postDeleteUseCase.delete(postAccountId);
        postDeleteUseCase.delete(postAnonymousId);
        //then
        Assertions.assertThat(postJpaRepository.findById(postAccountId).isEmpty()).isEqualTo(true);
        Assertions.assertThat(postJpaRepository.findById(postAnonymousId).isEmpty()).isEqualTo(true);
    }
}