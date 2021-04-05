package com.se.apiserver.v1.attach.domain.usecase;


import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.domain.error.AttachErrorCode;
import com.se.apiserver.v1.attach.infra.dto.AttachCreateDto;
import com.se.apiserver.v1.attach.infra.dto.AttachReadDto;
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

@SpringBootTest
@Transactional
class AttachCreateUseCaseTest {
  @Autowired
  AttachJpaRepository attachJpaRepository;
  @Autowired
  AttachCreateUseCase attachCreateUseCase;
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

  private void initData() {
    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(1,
            "1", Arrays.asList(new SimpleGrantedAuthority("freeboard"))));

    board = new Board("freeboard", "자유게시판");
    boardJpaRepository.save(board);

    post = new Post(new Anonymous("익명1", "testtest"), board,
            new PostContent("test.....", "title..."), PostIsNotice.NORMAL, PostIsSecret.NORMAL
            ,new HashSet<>(Arrays.asList("freeboard")) ,
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

  }

  @Test
  void 게시글_첨부파일_성공() {
    //given
    initData();
    
    //when
    AttachReadDto.Response response = attachCreateUseCase.create(AttachCreateDto.Request.builder()
            .downloadUrl("download")
            .fileName("filename")
            .postId(post.getPostId())
            .build());
    //then
    Assertions.assertThat(response.getDownloadUrl()).isEqualTo("download");
    Assertions.assertThat(response.getFileName()).isEqualTo("filename");
    Assertions.assertThat(response.getPostId()).isEqualTo(post.getPostId());
    Assertions.assertThat(post.getAttaches().size()).isEqualTo(1);
  }

  @Test
  void 댓글_첨부파일_성공() {
    //given
    initData();

    //when
    AttachReadDto.Response response = attachCreateUseCase.create(AttachCreateDto.Request.builder()
            .downloadUrl("download")
            .fileName("filename")
            .replyId(reply.getReplyId())
            .build());
    //then
    Assertions.assertThat(response.getDownloadUrl()).isEqualTo("download");
    Assertions.assertThat(response.getFileName()).isEqualTo("filename");
    Assertions.assertThat(response.getReplyId()).isEqualTo(reply.getReplyId());
    Assertions.assertThat(reply.getAttaches().size()).isEqualTo(1);
  }

  @Test
  void 미설정_첨부파일_성공() {
    //given
    initData();

    //when
    AttachReadDto.Response response = attachCreateUseCase.create(AttachCreateDto.Request.builder()
            .downloadUrl("download")
            .fileName("filename")
            .build());
    //then
    Assertions.assertThat(response.getDownloadUrl()).isEqualTo("download");
    Assertions.assertThat(response.getFileName()).isEqualTo("filename");
  }

  @Test
  void 댓글_게시글_동시설정_첨부파일_실패() {
    //given
    initData();

    //when
    //then
    Assertions.assertThatThrownBy(() -> {
      attachCreateUseCase.create(AttachCreateDto.Request.builder()
              .downloadUrl("download")
              .fileName("filename")
              .replyId(reply.getReplyId())
              .postId(post.getPostId())
              .build());
    }).isInstanceOf(BusinessException.class).hasMessage(AttachErrorCode.INVALID_INPUT.getMessage());
  }

}