package com.se.apiserver.v1.post.application.service;

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
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.dto.PostDeleteDto;
import com.se.apiserver.v1.post.application.dto.PostDeleteDto.AnonymousPostDeleteRequest;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsDeleted;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class PostDeleteServiceTest {

  @Mock
  private PostJpaRepository postJpaRepository;
  @Mock
  private AccountContextService accountContextService;
  @Mock
  private PasswordEncoder passwordEncoder;
  @InjectMocks
  private PostDeleteService postDeleteService;

  @Test
  void ??????_?????????_??????_??????() {
    // given
    Long postId = 1L;
    Account account = getAccount();
    Set<String> authorities = Set.of("FREEBOARD_ACCESS");
    Post post = new Post(account
        , getBoard()
        , getPostContent()
        , PostIsNotice.NORMAL
        , PostIsSecret.NORMAL
        , authorities
        , new ArrayList<>()
        , new ArrayList<>()
        , "127.0.0.1");
    given(accountContextService.getContextAccount()).willReturn(account);
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));
    given(postJpaRepository.save(post)).willReturn(post);

    // when
    boolean result = postDeleteService.delete(postId);

    // then
    assertThat(result, is(true));
    assertThat(post.getPostIsDeleted(), is(PostIsDeleted.DELETED));
  }

  @Test
  void ??????_?????????_?????????_??????_??????() {
    // given
    Long postId = 1L;
    Anonymous anonymous = getAnonymous();
    Set<String> authorities = Set.of("FREEBOARD_ACCESS");
    Post post = new Post(anonymous
        , getBoard()
        , getPostContent()
        , PostIsNotice.NORMAL
        , PostIsSecret.NORMAL
        , authorities
        , new ArrayList<>()
        , new ArrayList<>()
        , "127.0.0.1");
    PostDeleteDto.AnonymousPostDeleteRequest anonymousPostDeleteRequest
        = AnonymousPostDeleteRequest
        .builder()
        .postId(postId)
        .anonymousPassword("qwerty")
        .build();
    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));
    given(passwordEncoder.matches("qwerty", post.getAnonymousPassword())).willReturn(true);
    given(postJpaRepository.save(post)).willReturn(post);
    // when
    boolean result = postDeleteService.delete(anonymousPostDeleteRequest);

    // then
    assertThat(result, is(true));
    assertThat(post.getPostIsDeleted(), is(PostIsDeleted.DELETED));
  }

  @Test
  void ??????_?????????_????????????_????????????_?????????_??????_??????() {
    // given
    Long postId = 1L;
    Anonymous anonymous = getAnonymous();
    Set<String> authorities = Set.of("FREEBOARD_ACCESS");
    Post post = new Post(anonymous
        , getBoard()
        , getPostContent()
        , PostIsNotice.NORMAL
        , PostIsSecret.NORMAL
        , authorities
        , new ArrayList<>()
        , new ArrayList<>()
        , "127.0.0.1");
    PostDeleteDto.AnonymousPostDeleteRequest anonymousPostDeleteRequest
        = AnonymousPostDeleteRequest
        .builder()
        .postId(postId)
        .anonymousPassword("qwerty")
        .build();
    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));
    given(passwordEncoder.matches(anonymous.getAnonymousPassword(), post.getAnonymousPassword())).willReturn(false);

      // when
    BusinessException businessException
        = assertThrows(BusinessException.class, () -> postDeleteService.delete(anonymousPostDeleteRequest));

    // then
    assertThat(businessException.getErrorCode(), is(PostErrorCode.ANONYMOUS_PASSWORD_INCORRECT));
    assertThat(businessException.getMessage(), is("?????? ????????? ??????????????? ???????????????"));
  }

  @Test
  void ????????????_??????_?????????() {
    // given
    Long postId = 1L;
    Account account = getAccount();
    Set<String> authorities = Set.of("FREEBOARD_ACCESS");
    given(accountContextService.getContextAccount()).willReturn(account);
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.findById(postId))
        .willThrow(new BusinessException(PostErrorCode.NO_SUCH_POST));

    // when
    BusinessException businessException
        = assertThrows(BusinessException.class, () -> postDeleteService.delete(postId));

    // then
    assertThat(businessException.getErrorCode(), is(PostErrorCode.NO_SUCH_POST));
    assertThat(businessException.getMessage(), is("???????????? ?????? ?????????"));
  }

  @Test
  void ?????????_??????_??????_??????() {
    // given
    Long postId = 1L;
    Set<String> authorities = Set.of("FREEBOARD_ACCESS");
    Post post = new Post(getAccount()
        , getBoard()
        , getPostContent()
        , PostIsNotice.NORMAL
        , PostIsSecret.NORMAL
        , authorities
        , new ArrayList<>()
        , new ArrayList<>()
        , "127.0.0.1");
    Account newAccount = new Account(1L
        , "idString"
        , "password"
        , "name"
        , "nickname"
        , "studentId"
        , AccountType.STUDENT
        , "phoneNumber"
        , "se@kkumoh.ac.kr"
        , "lastSingInIp"
        , InformationOpenAgree.AGREE
        , null
        , null);
    given(accountContextService.getContextAccount()).willReturn(getAccount());
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));


    // when
    BusinessException businessException
        = assertThrows(BusinessException.class, () -> postDeleteService.delete(postId));

    // then
    assertThat(businessException.getErrorCode(), is(AccountErrorCode.CAN_NOT_ACCESS_ACCOUNT));
    assertThat(businessException.getMessage(), is("?????? ????????? ????????? ??? ????????????"));
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
}
