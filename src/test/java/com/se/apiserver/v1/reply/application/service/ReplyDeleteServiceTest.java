package com.se.apiserver.v1.reply.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.reply.application.dto.ReplyDeleteDto;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsDelete;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class ReplyDeleteServiceTest {

  @Mock
  private ReplyJpaRepository replyJpaRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private AccountContextService accountContextService;

  @InjectMocks
  private ReplyDeleteService replyDeleteService;

  @Test
  void 회원이_본인_댓글_삭제_성공() {
    // given
    Long replyId = 1L;
    List<Attach> attachList = new ArrayList<>();
    Reply reply = new Reply(getPost()
        , "댓글"
        , ReplyIsSecret.SECRET
        , attachList
        , null,
        "127.0.0.1"
        ,getAccount());
    Reply child1 = new Reply(getPost()
        , "대댓글1"
        , ReplyIsSecret.SECRET
        , attachList
        , reply,
        "127.0.0.1"
        ,getAnonymous());
    Reply child2 = new Reply(getPost()
        , "대댓글2"
        , ReplyIsSecret.SECRET
        , attachList
        , reply,
        "127.0.0.1"
        ,getAnonymous());

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getCurrentAccountId()).willReturn(getAccount().getAccountId());
    given(accountContextService.getContextAuthorities()).willReturn(new HashSet<>());
    given(replyJpaRepository.save(reply)).willReturn(Mockito.any(Reply.class));

    // when
    Boolean isDeleted = replyDeleteService.delete(replyId);

    // then
    assertThat(isDeleted, is(true));
    assertThat(reply.getIsDelete(), is(ReplyIsDelete.DELETED));
    assertThat(child1.getIsDelete(), is(ReplyIsDelete.NORMAL));
    assertThat(child2.getIsDelete(), is(ReplyIsDelete.NORMAL));
  }

  @Test
  void 관리자가_댓글_삭제_성공() {
    // given
    Long replyId = 1L;
    List<Attach> attachList = new ArrayList<>();
    Reply reply = new Reply(getPost()
        , "댓글"
        , ReplyIsSecret.SECRET
        , attachList
        , null,
        "127.0.0.1"
        ,getAccount());
    Account manager = new Account(2L
        , "manager"
        , "password"
        , "관리자"
        , "manager"
        , "00000001"
        , AccountType.ASSISTANT,
        "01011111111"
        , "admin@kumoh.ac.kr"
        , "127.0.0.1"
        , InformationOpenAgree.AGREE
        , null
        , null);

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getCurrentAccountId()).willReturn(manager.getAccountId());
    given(accountContextService.getContextAuthorities()).willReturn(new HashSet<>(Arrays.asList("REPLY_MANAGE")));
    given(replyJpaRepository.save(reply)).willReturn(Mockito.any(Reply.class));

    // when
    Boolean isDeleted = replyDeleteService.delete(replyId);

    // then
    assertThat(isDeleted, is(true));
  }

  @Test
  void 로그인_안한_사용자가_댓글_삭제_시_삭제_실패() {
    // given
    Long replyId = 1L;
    List<Attach> attachList = new ArrayList<>();
    Reply reply = new Reply(getPost()
        , "댓글"
        , ReplyIsSecret.SECRET
        , attachList
        , null,
        "127.0.0.1"
        ,getAccount());

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(accountContextService.isSignIn()).willReturn(false);

    // when
    AccessDeniedException accessDeniedException
        = assertThrows(AccessDeniedException.class, () -> replyDeleteService.delete(replyId));

    // then
    assertThat(accessDeniedException.getMessage(), is("로그인 후 접근 가능"));
  }

  @Test
  void 익명_사용자가_댓글_삭제_성공() {
    // given
    Long replyId = 1L;
    String password = "qwerty";
    List<Attach> attachList = new ArrayList<>();
    Reply reply = new Reply(getPost()
        , "댓글"
        , ReplyIsSecret.SECRET
        , attachList
        , null,
        "127.0.0.1"
        ,getAnonymous());
    ReplyDeleteDto.AnonymousReplyDeleteRequest request
        = ReplyDeleteDto.AnonymousReplyDeleteRequest.builder().replyId(replyId).password(password).build();

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(accountContextService.getContextAuthorities()).willReturn(new HashSet<>());
    given(passwordEncoder.matches(password, reply.getAnonymous().getAnonymousPassword())).willReturn(true);
    given(replyJpaRepository.save(reply)).willReturn(Mockito.any(Reply.class));

    // when
    Boolean isDeleted = replyDeleteService.deleteAnonymousReply(request);

    // then
    assertThat(isDeleted, is(true));
    assertThat(reply.getIsDelete(), is(ReplyIsDelete.DELETED));
  }

  @Test
  void 익명_사용자_비밀번호_불일치() {
    // given
    Long replyId = 1L;
    String password = "password";
    List<Attach> attachList = new ArrayList<>();
    Reply reply = new Reply(getPost()
        , "댓글"
        , ReplyIsSecret.SECRET
        , attachList
        , null,
        "127.0.0.1"
        ,getAnonymous());
    ReplyDeleteDto.AnonymousReplyDeleteRequest request
        = ReplyDeleteDto.AnonymousReplyDeleteRequest.builder().replyId(replyId).password(password).build();

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(accountContextService.getContextAuthorities()).willReturn(new HashSet<>());
    given(passwordEncoder.matches(password, reply.getAnonymous().getAnonymousPassword())).willThrow(new BusinessException(ReplyErrorCode.INVALID_PASSWORD));

    // when
    BusinessException businessException
        = assertThrows(BusinessException.class, () -> replyDeleteService.deleteAnonymousReply(request));

    // then
    assertThat(businessException.getMessage(), is("익명 사용자 패스워드 틀림"));
    assertThat(businessException.getErrorCode(), is(ReplyErrorCode.INVALID_PASSWORD));
  }

  @Test
  void 존재하지_않는_댓글(){
    // given
    Long replyId = 1L;
    given(replyJpaRepository.findById(replyId)).willThrow(new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));

    // when
    BusinessException businessException = assertThrows(BusinessException.class, () -> replyDeleteService.delete(replyId));

    // then
    assertThat(businessException.getErrorCode(), is(ReplyErrorCode.NO_SUCH_REPLY));
    assertThat(businessException.getMessage(), is("존재하지 않는 댓글"));
  }

  @Test
  void 권한_없는_사용자가_댓글_삭제_요청_시_삭제_실패() {
    // given
    Long replyId = 1L;
    List<Attach> attachList = new ArrayList<>();
    Set<String> authorities = new HashSet<>();
    Reply reply = new Reply(getPost()
        , "댓글"
        , ReplyIsSecret.SECRET
        , attachList
        , null,
        "127.0.0.1"
        ,getAccount());
    Account thirdParty = new Account(2L
        , "idString"
        , "password"
        , "이용자"
        , "User"
        , "20180000"
        , AccountType.STUDENT
        , "01000000000"
        , "se@kumoh.ac.kr"
        , "127.0.0.1"
        , InformationOpenAgree.AGREE
        , null
        , null);

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getCurrentAccountId()).willReturn(thirdParty.getAccountId());
    given(accountContextService.getContextAuthorities()).willReturn(authorities);

    // when
    AccessDeniedException accessDeniedException = assertThrows(AccessDeniedException.class, () -> replyDeleteService.delete(replyId));

    // then
    assertThat(accessDeniedException.getMessage(), is("본인 혹은 관리자만 접근 가능"));
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

  private Account getAccount() {
    Long accountId = 1L;
    String idString = "jduck1024";
    String password = "1234";
    String name = "윤진";
    String nickname = "오리";
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

  private Anonymous getAnonymous() {
    String anonymousNickName = "ㅇㅇ";
    String anonymousPassword = "qwerty";
    return new Anonymous(anonymousNickName, anonymousPassword);
  }

  private PostContent getPostContent() {
    String title = "학생회 특식 배부";
    String text = "양식 : 학년 / 학번 이름";

    return new PostContent(title, text);
  }

  private Board getBoard() {
    String nameEng = "FREEBOARD";
    String nameKor = "자유게시판";

    return new Board(nameEng, nameKor);
  }
}
