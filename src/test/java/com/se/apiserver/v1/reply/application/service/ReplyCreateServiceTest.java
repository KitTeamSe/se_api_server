package com.se.apiserver.v1.reply.application.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.application.dto.ReplyCreateDto;
import com.se.apiserver.v1.reply.application.dto.ReplyCreateDto.Request;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsSecret;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class ReplyCreateServiceTest {

  @Mock
  private ReplyJpaRepository replyJpaRepository;

  @Mock
  private PostJpaRepository postJpaRepository;

  @Mock
  private AccountContextService accountContextService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private ReplyCreateService replyCreateService;

  @Test
  void 회원_댓글_등록_성공() {
    // given
    Long postId = 1L;
    ReplyCreateDto.Request request = ReplyCreateDto.Request.builder()
        .postId(postId)
        .text("20180764 이름")
        .isSecret(ReplyIsSecret.NORMAL)
        .build();
    Post post = getPost();
    Account account = getAccount();
    Reply reply = new Reply(post
        , request.getText()
        , request.getIsSecret()
        , null
        , null
        , "127.0.0.1"
        , account);

    Set<String> authorities = new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"));

    // 수정 요망
    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getContextAccount()).willReturn(account);
    given(replyJpaRepository.save(Mockito.any(Reply.class))).willReturn(reply);

    // when, then
    assertDoesNotThrow(() -> replyCreateService.create(request));
  }

  @Test
  void 익명_사용자_댓글_등록_성공() {
    // given
    Long postId = 1L;
    Anonymous anonymous = getAnonymous();
    ReplyCreateDto.Request request = ReplyCreateDto.Request.builder()
        .postId(postId)
        .text("20180764 이름")
        .anonymous(anonymous)
        .isSecret(ReplyIsSecret.NORMAL)
        .build();
    Post post = getPost();
    List<Attach> attachList = new ArrayList<>();
    Reply reply = new Reply(post
        , request.getText()
        , request.getIsSecret()
        , attachList
        , null,
        "127.0.0.1"
        , anonymous);
    Set<String> authorities = new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"));

    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(accountContextService.isSignIn()).willReturn(false);
    given(replyJpaRepository.save(Mockito.any(Reply.class))).willReturn(reply);
    given(passwordEncoder.encode(request.getAnonymous().getAnonymousPassword()))
        .willReturn("iDonTKnOw!@#");

    // when, then
    assertDoesNotThrow(() -> replyCreateService.create(request));
  }

  @Test
  void 대댓글_등록할_때_부모_댓글_존재하지_않는_경우()
      throws NoSuchMethodException {
    // given
    Long nonExistentReplyId = 1L;
    given(replyJpaRepository.findById(nonExistentReplyId)).willThrow(new BusinessException(
        ReplyErrorCode.NO_SUCH_REPLY));
    Method method = replyCreateService.getClass().getDeclaredMethod("getParent", Long.class);
    method.setAccessible(true);

    // when
    InvocationTargetException invocationTargetException = assertThrows(
        InvocationTargetException.class,
        () -> method.invoke(replyCreateService, nonExistentReplyId));
    BusinessException businessException = (BusinessException) invocationTargetException
        .getTargetException();

    // then
    assertThat(businessException.getErrorCode(), is(ReplyErrorCode.NO_SUCH_REPLY));
    assertThat(businessException.getMessage(), is("존재하지 않는 댓글"));
  }

  @Test
  void 존재하지_않는_게시글() {
    // given
    Long nonExistentPostId = 2L;
    String text = "20180764 이름";
    ReplyCreateDto.Request request = ReplyCreateDto.Request.builder()
        .postId(nonExistentPostId)
        .text(text)
        .isSecret(ReplyIsSecret.NORMAL)
        .build();
    given(postJpaRepository.findById(nonExistentPostId))
        .willThrow(new BusinessException(PostErrorCode.NO_SUCH_POST));

    // when
    BusinessException businessException = assertThrows(BusinessException.class,
        () -> replyCreateService
            .create(request));

    // then
    assertThat(businessException.getErrorCode(), is(PostErrorCode.NO_SUCH_POST));
    assertThat(businessException.getMessage(), is("존재하지 않는 게시글"));
  }

  @Test
  void 접근_권한이_없는_게시판() {
    // given
    Post post = getPost();
    Long postId = 1L;
    String text = "20180764 이름";
    Request request = Request.builder()
        .postId(postId)
        .text(text)
        .isSecret(ReplyIsSecret.NORMAL)
        .build();
    given(postJpaRepository.findById(postId)).willReturn(Optional.of(post));

    // when
    AccessDeniedException accessDeniedException = assertThrows(AccessDeniedException.class,
        () -> replyCreateService.create(request));

    // then
    assertThat(accessDeniedException.getMessage(), is("접근 권한이 없습니다"));
  }

  @Test
  void 삭제된_게시글() {
    // given
    Post post = getPost();
    post.delete();
    Long postId = 1L;
    String text = "20180764 이름";
    ReplyCreateDto.Request request = ReplyCreateDto.Request.builder()
        .postId(postId)
        .text(text)
        .isSecret(ReplyIsSecret.NORMAL)
        .build();

    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));

    // when
    BusinessException businessException = assertThrows(BusinessException.class,
        () -> replyCreateService.create(request));

    // then
    assertThat(businessException.getMessage(), is("삭제된 게시글입니다"));
    assertThat(businessException.getErrorCode(), is(PostErrorCode.DELETED_POST));
  }

  @Test
  void 대댓글_작성_실패() {
    // given
    Long postId = 1L;
    ReplyCreateDto.Request request = ReplyCreateDto.Request.builder()
        .postId(postId)
        .text("20180764 이름")
        .isSecret(ReplyIsSecret.NORMAL)
        .parentId(1L)
        .build();
    Post post = getPost();
    Account account = getAccount();
    Set<String> authorities = new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"));

    Reply parent = new Reply(post
        , "Parent Reply"
        , ReplyIsSecret.NORMAL
        , null
        , null
        , "127.0.0.1"
        , account);
    Reply child = new Reply(post
        , "Child Reply"
        , ReplyIsSecret.NORMAL
        , null
        , parent
        , "127.0.0.1"
        , account);

    given(postJpaRepository.findById(postId)).willReturn(Optional.of(post));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(replyJpaRepository.findById(1L)).willReturn(Optional.of(child));

    // when
    BusinessException businessException = assertThrows(BusinessException.class,
        () -> replyCreateService.create(request));

    // then
    assertThat(businessException.getErrorCode(), is(ReplyErrorCode.INVALID_REPLY));
    assertThat(businessException.getMessage(), is("대댓글의 댓글은 작성할 수 없습니다"));
  }

  private Anonymous getAnonymous() {
    String anonymousNickName = "ㅇㅇ";
    String anonymousPassword = "qwerty";
    return new Anonymous(anonymousNickName, anonymousPassword);
  }

  private Account getAccount() {
    Long accountId = 1L;
    String idString = "jduck1024";
    String password = "1234";
    String name = "이름";
    String nickname = "닉네임";
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

  private Board getBoard() {
    String nameEng = "FREEBOARD";
    String nameKor = "자유게시판";

    return new Board(nameEng, nameKor);
  }

  private PostContent getPostContent() {
    String title = "학생회 특식 배부";
    String text = "양식 : 학년 / 학번 이름";

    return new PostContent(title, text);
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