package com.se.apiserver.v1.reply.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.application.dto.ReplyReadDto;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsSecret;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReplyReadServiceTest {

  @Mock
  private ReplyJpaRepository replyJpaRepository;

  @Mock
  private AccountContextService accountContextService;

  @Mock
  private PostJpaRepository postJpaRepository;

  @InjectMocks
  ReplyReadService replyReadService;

  @Test
  void ?????????_?????????_??????_??????_??????_??????() {
    // given
    Long replyId = 1L;
    List<Attach> attachList = new ArrayList<>();
    Reply reply = new Reply(getPost()
        , "=====??????====="
        , ReplyIsSecret.NORMAL
        , attachList
        , null
        , "127.0.0.1"
        , getAccount());
    Set<String> authorities = new HashSet<>(Arrays.asList("REPLY_MANAGE"));

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);

    // when
    ReplyReadDto.Response response = replyReadService.read(replyId);

    // then
    assertThat(response.getAccountId(), is("jduck1024"));
    assertThat(response.getText(), is("=====??????====="));
  }

  @Test
  void ?????????_?????????_??????_??????_????????????_??????() {
    // given
    Long replyId = 1L;
    List<Attach> attachList = new ArrayList<>();
    Reply reply = new Reply(getPost()
        , "=====??????====="
        , ReplyIsSecret.SECRET
        , attachList
        , null
        , "127.0.0.1"
        , getAccount());
    Set<String> authorities = new HashSet<>();

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(accountContextService.isSignIn()).willReturn(false);
    given(accountContextService.getContextAuthorities()).willReturn(authorities);

    // when
    ReplyReadDto.Response response = replyReadService.read(replyId);

    // then
    assertThat(response.getAccountId(), is("jduck1024"));
    assertThat(response.getText(), is("?????? ???????????????."));
  }

  @Test
  void ????????????_??????_????????????_?????????_??????_??????() {
    // given
    Long replyId = 1L;
    List<Attach> attachList = new ArrayList<>();
    Reply reply = new Reply(getPost()
        , "=====??????====="
        , ReplyIsSecret.SECRET
        , attachList
        , null
        , "127.0.0.1"
        , getAccount());
    reply.delete();
    Set<String> authorities = new HashSet<>();

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getContextAccount()).willReturn(getAccount());

    // when
    ReplyReadDto.Response response = replyReadService.read(replyId);

    // then
    assertThat(response.getAccountId(), is("jduck1024"));
    assertThat(response.getText(), is("???????????? ?????? ????????? ???????????????."));
  }

  @Test
  void ????????????_?????????_??????_??????() {
    // given
    Long replyId = 1L;
    List<Attach> attachList = new ArrayList<>();
    Reply reply = new Reply(getPost()
        , "=====??????====="
        , ReplyIsSecret.SECRET
        , attachList
        , null
        , "127.0.0.1"
        , getAccount());
    reply.delete();
    Set<String> authorities = new HashSet<>(Arrays.asList("REPLY_MANAGE"));

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getContextAccount()).willReturn(getAccount());

    // when
    ReplyReadDto.Response response = replyReadService.read(replyId);

    // then
    assertThat(response.getAccountId(), is("jduck1024"));
    assertThat(response.getText(), is("=====??????====="));
  }

  @Test
  void ????????????_??????_??????_??????() {
    // given
    Long replyId = 1L;
    given(replyJpaRepository.findById(replyId))
        .willThrow(new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));

    // when
    BusinessException businessException = assertThrows(BusinessException.class,
        () -> replyReadService.read(replyId));

    // then
    assertThat(businessException.getErrorCode(), is(ReplyErrorCode.NO_SUCH_REPLY));
    assertThat(businessException.getMessage(), is("???????????? ?????? ??????"));
  }

  @Test
  void ??????_??????_????????????_?????????_??????_??????_??????() {
    // given
    Long replyId = 1L;
    List<Attach> attachList = new ArrayList<>();
    Reply reply = new Reply(getPost()
        , "=====??????====="
        , ReplyIsSecret.SECRET
        , attachList
        , null
        , "127.0.0.1"
        , getAccount());
    Set<String> authorities = new HashSet<>();

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getContextAccount()).willReturn(getAccount());

    // when
    ReplyReadDto.Response response = replyReadService.read(replyId);

    // then
    assertThat(response.getAccountId(), is("jduck1024"));
    assertThat(response.getText(), is("=====??????====="));
  }

  @Test
  void ?????????_????????????_??????_????????????_??????_??????_??????() {
    // given
    Long replyId = 1L;
    List<Attach> attachList = new ArrayList<>();
    Account commenter = new Account(2L
        , "commenter"
        , "commenterpw"
        , "?????????"
        , "?????? ??? ?????? ??????"
        , "20181818",
        AccountType.STUDENT,
        "01012341234"
        , "secret@kumoh.ac.kr"
        , "127.0.0.1"
        , InformationOpenAgree.AGREE
        , null
        , null);

    Reply reply = new Reply(getPost()
        , "=====??????====="
        , ReplyIsSecret.SECRET
        , attachList
        , null
        , "127.0.0.1"
        , commenter);
    Set<String> authorities = new HashSet<>();

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getContextAccount()).willReturn(getAccount());

    // when
    ReplyReadDto.Response response = replyReadService.read(replyId);

    // then
    assertThat(response.getAccountId(), is("commenter"));
    assertThat(response.getText(), is("=====??????====="));
  }


  private Board getBoard() {
    String nameEng = "FREEBOARD";
    String nameKor = "???????????????";

    return new Board(nameEng, nameKor);
  }

  private PostContent getPostContent() {
    String title = "????????? ?????? ??????";
    String text = "?????? : ?????? / ?????? ??????";

    return new PostContent(title, text);
  }

  private Account getAccount() {
    Long accountId = 1L;
    String idString = "jduck1024";
    String password = "1234";
    String name = "??????";
    String nickname = "??????";
    String studentId = "20180764";
    AccountType accountType = AccountType.STUDENT;
    String phoneNumber = "01012345678";
    String email = "20180764@kumoh.ac.kr";
    String lastSingInIp = "127.0.0.1";
    InformationOpenAgree informationOpenAgree = InformationOpenAgree.AGREE;
    Question question = null;
    String answer = null;

    return new Account(accountId, idString, password, name, nickname, studentId, accountType,
        phoneNumber, email, lastSingInIp, informationOpenAgree, question, answer);
  }

  private Post getPost() {
    Account account = getAccount();
    Board board = getBoard();
    PostContent postContent = getPostContent();
    Set<String> authorities = new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"));
    List<Tag> tags = new ArrayList<>();
    List<Attach> attaches = new ArrayList<>();
    String createdIp = "127.0.0.1";

    return new Post(account, board, postContent, PostIsNotice.NORMAL, PostIsSecret.NORMAL,
        authorities, tags, attaches, createdIp);
  }
}
