package com.se.apiserver.v1.reply.application.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.attach.application.dto.AttachReadDto;
import com.se.apiserver.v1.attach.application.service.AttachCreateService;
import com.se.apiserver.v1.attach.application.service.AttachDeleteService;
import com.se.apiserver.v1.attach.application.service.AttachUpdateService;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.application.dto.ReplyUpdateDto;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsSecret;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import java.nio.charset.StandardCharsets;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ReplyUpdateServiceTest {

  private String data = "data";
  private MultipartFile[] files = {
      new MockMultipartFile("file"
          , "file.png"
          , "text/plain"
          , data.getBytes(StandardCharsets.UTF_8)),
      new MockMultipartFile("file"
          , "file.png"
          , "text/plain"
          , data.getBytes(StandardCharsets.UTF_8)),
  };

  @Mock
  private ReplyJpaRepository replyJpaRepository;
  @Mock
  private PostJpaRepository postJpaRepository;
  @Mock
  private AccountContextService accountContextService;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private AttachUpdateService attachUpdateService;
  @Mock
  private AttachJpaRepository attachJpaRepository;
  @InjectMocks
  private ReplyUpdateService replyUpdateService;

  @Test
  void ??????_??????_??????_??????() {
    // given
    Long postId = 1L;
    Long replyId = 1L;
    List<Long> attachIdList = Arrays.asList(1L);
    ReplyUpdateDto.Request request = ReplyUpdateDto.Request
        .builder()
        .replyId(replyId)
        .password("1234")
        .postId(postId)
        .text("??????????????????????????????")
        .isSecret(ReplyIsSecret.NORMAL)
        .build();
    Post post = getPost();
    Anonymous anonymous = getAnonymous();
    Reply reply = new Reply(post
        , request.getText()
        , request.getIsSecret()
        , null
        , null
        , "127.0.0.1"
        , anonymous);
    Reply savedReply = reply;
    Set<String> authorities = new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"));

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(
        passwordEncoder.matches(request.getPassword(), reply.getAnonymous().getAnonymousPassword()))
        .willReturn(true);
    willDoNothing().given(attachUpdateService).update(null, reply.getReplyId(), files);
    given(replyJpaRepository.save(reply)).willReturn(any(Reply.class));
    given(replyJpaRepository.save(reply)).willReturn(savedReply);

    // when, then
    assertDoesNotThrow(() -> replyUpdateService.update(request, files));
  }

  @Test
  void ??????_??????_??????_??????() {
    // given
    Long postId = 1L;
    Long replyId = 1L;
    List<Long> attachIdList = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
    ReplyUpdateDto.Request request = ReplyUpdateDto.Request
        .builder()
        .replyId(replyId)
        .password("1234")
        .postId(postId)
        .text("??????????????????????????????")
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
    Reply savedReply = reply;
    MultipartFile[] files = new MultipartFile[1];
    Set<String> authorities = new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"));

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(accountContextService.getCurrentAccountId()).willReturn(account.getAccountId());
    willDoNothing().given(attachUpdateService).update(null, reply.getReplyId(), files);
    given(replyJpaRepository.save(reply)).willReturn(reply);
    given(replyJpaRepository.save(reply)).willReturn(savedReply);

    // when, then
    assertDoesNotThrow(() -> replyUpdateService.update(request, files));
  }

  @Test
  void ??????_?????????_??????_??????_??????_???_??????_??????() {
    // given
    Long postId = 1L;
    Long replyId = 1L;

    ReplyUpdateDto.Request request = ReplyUpdateDto.Request
        .builder()
        .replyId(replyId)
        .password("1234")
        .postId(postId)
        .text("??????????????????????????????")
        .isSecret(ReplyIsSecret.NORMAL)
        .build();
    Post post = getPost();
    Account account = getAccount();
    Long anotherAccountId = 2L;
    Reply reply = new Reply(post
        , request.getText()
        , request.getIsSecret()
        , null
        , null
        , "127.0.0.1"
        , account);
    MultipartFile[] files = new MultipartFile[1];
    List<AttachReadDto.Response> dtoResponse
        = new ArrayList<>(Arrays.asList(
        AttachReadDto.Response
            .builder()
            .attachId(1L)
            .replyId(replyId)
            .downloadUrl("URL")
            .fileName("file.jpg").build()));
    Set<String> authorities = new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"));

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(accountContextService.getCurrentAccountId()).willReturn(anotherAccountId);

    // when
    AccessDeniedException accessDeniedException = assertThrows(AccessDeniedException.class,
        () -> replyUpdateService
            .update(request, files));
    // then
    assertThat(accessDeniedException.getMessage(), is("????????? ????????? ?????? ???????????????"));
  }

  @Test
  void ????????????_??????_??????() {
    // given
    Long replyId = 1L;
    Long postId = 1L;
    ReplyUpdateDto.Request request = ReplyUpdateDto.Request
        .builder()
        .replyId(replyId)
        .password("1234")
        .postId(postId)
        .text("??????????????????????????????")
        .isSecret(ReplyIsSecret.NORMAL)
        .build();
    Post post = getPost();
    MultipartFile[] files = new MultipartFile[1];
    Reply reply = new Reply(post
        , request.getText()
        , request.getIsSecret()
        , null
        , null
        , "127.0.0.1"
        , getAnonymous());
    Long nonExistentReplyId = 2L;

    given(replyJpaRepository.findById(replyId))
        .willThrow(new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));

    // when
    BusinessException businessException = assertThrows(BusinessException.class,
        () -> replyUpdateService
            .update(request, files));
    // then
    assertThat(businessException.getErrorCode(), is(ReplyErrorCode.NO_SUCH_REPLY));
    assertThat(businessException.getMessage(), is("???????????? ?????? ??????"));
  }

  @Test
  void ????????????_??????_?????????() {
    // given
    Long replyId = 1L;
    Long nonExistentPostId = 2L;
    ReplyUpdateDto.Request request = ReplyUpdateDto.Request
        .builder()
        .replyId(replyId)
        .password("1234")
        .postId(nonExistentPostId)
        .text("??????????????????????????????")
        .isSecret(ReplyIsSecret.NORMAL)

        .build();
    Post post = getPost();
    MultipartFile[] files = new MultipartFile[1];
    Reply reply = new Reply(post
        , request.getText()
        , request.getIsSecret()
        , null
        , null
        , "127.0.0.1"
        , getAnonymous());

    given(replyJpaRepository.findById(replyId)).willReturn(Optional.of(reply));
    given(postJpaRepository.findById(nonExistentPostId))
        .willThrow(new BusinessException(PostErrorCode.NO_SUCH_POST));

    // when
    BusinessException businessException = assertThrows(BusinessException.class,
        () -> replyUpdateService
            .update(request, files));

    // then
    assertThat(businessException.getErrorCode(), is(PostErrorCode.NO_SUCH_POST));
    assertThat(businessException.getMessage(), is("???????????? ?????? ?????????"));
  }

  @Test
  void ??????_??????_????????????_?????????() {
    // given
    Long postId = 1L;
    Long replyId = 1L;
    ReplyUpdateDto.Request request = ReplyUpdateDto.Request
        .builder()
        .replyId(replyId)
        .password("wrong")
        .postId(postId)
        .text("??????????????????????????????")
        .isSecret(ReplyIsSecret.NORMAL)

        .build();
    Post post = getPost();
    Anonymous anonymous = getAnonymous();
    Reply reply = new Reply(post
        , request.getText()
        , request.getIsSecret()
        , null
        , null
        , "127.0.0.1"
        , anonymous);
    MultipartFile[] files = new MultipartFile[1];
    List<AttachReadDto.Response> dtoResponse
        = new ArrayList<>(Arrays.asList(
        AttachReadDto.Response
            .builder()
            .attachId(1L)
            .replyId(replyId)
            .downloadUrl("URL")
            .fileName("file.jpg").build()));
    Set<String> authorities = new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"));

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(
        passwordEncoder.matches(request.getPassword(), reply.getAnonymous().getAnonymousPassword()))
        .willThrow(new BusinessException(ReplyErrorCode.INVALID_PASSWORD));

    // when
    BusinessException businessException = assertThrows(BusinessException.class,
        () -> replyUpdateService.update(request, files));

    // then
    assertThat(businessException.getErrorCode(), is(ReplyErrorCode.INVALID_PASSWORD));
    assertThat(businessException.getMessage(), is("?????? ????????? ???????????? ??????"));
  }

  private Anonymous getAnonymous() {
    String anonymousNickName = "??????";
    String anonymousPassword = "qwerty";
    return new Anonymous(anonymousNickName, anonymousPassword);
  }

  private Account getAccount() {
    Long accountId = 1L;
    String idString = "jduck1024";
    String password = "1234";
    String name = "??????";
    String nickname = "?????????";
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
    String nameKor = "???????????????";

    return new Board(nameEng, nameKor);
  }

  private PostContent getPostContent() {
    String title = "????????? ?????? ??????";
    String text = "?????? : ?????? / ?????? ??????";

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