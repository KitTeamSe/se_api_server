package com.se.apiserver.v1.post.application.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.attach.application.service.AttachUpdateService;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.dto.PostCreateDto;
import com.se.apiserver.v1.post.application.dto.PostCreateDto.TagDto;
import com.se.apiserver.v1.post.application.dto.PostUpdateDto;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.tag.application.error.TagErrorCode;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import java.nio.charset.StandardCharsets;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class PostUpdateServiceTest {

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
  private PostJpaRepository postJpaRepository;
  @Mock
  private AccountContextService accountContextService;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private TagJpaRepository tagJpaRepository;
  @Mock
  private AttachJpaRepository attachJpaRepository;
  @Mock
  private AttachUpdateService attachUpdateService;
  @InjectMocks
  private PostUpdateService postUpdateService;

  @Test
  void ??????_?????????_??????_??????() {
    // given
    String ip = "127.0.0.1";
    Post post = new Post(
        getAccount()
        , getBoard()
        , getPostContent()
        , PostIsNotice.NORMAL
        , PostIsSecret.NORMAL
        , new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"))
        , new ArrayList<>()
        , new ArrayList<>()
        , ip);
    Post savedPost = post;
    PostUpdateDto.Request request = PostUpdateDto.Request
        .builder()
        .postId(1L)
        .postContent(new PostContent("??????????????????", "??????????????????"))
        .isNotice(PostIsNotice.NORMAL)
        .isSecret(PostIsSecret.NORMAL)
        .tagList(Arrays.asList(new PostCreateDto.TagDto(1L)))
        .build();
    Set<String> authorities = new HashSet<>();

    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.findById(request.getPostId())).willReturn(java.util.Optional.of(post));
    given(accountContextService.getCurrentClientIP()).willReturn(ip);
    willDoNothing().given(attachUpdateService).update(post.getPostId(), null, files);
    given(tagJpaRepository.findById(anyLong())).willReturn(java.util.Optional.of(new Tag("??????")));
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getCurrentAccountId()).willReturn(getAccount().getAccountId());
    given(postJpaRepository.save(post)).willReturn(savedPost);

    // when, then
    assertDoesNotThrow(() -> postUpdateService.update(request, files));
  }

  @Test
  void ??????_?????????_?????????_??????_??????() {
    // given
    String ip = "127.0.0.1";
    String password = "qwerty";
    Post post = new Post(
        getAnonymous()
        , getBoard()
        , getPostContent()
        , PostIsNotice.NORMAL
        , PostIsSecret.NORMAL
        , new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"))
        , new ArrayList<>()
        , new ArrayList<>()
        , ip);
    Post savedPost = post;
    PostUpdateDto.Request request = PostUpdateDto.Request
        .builder()
        .postId(1L)
        .postContent(new PostContent("??????????????????", "??????????????????"))
        .anonymousPassword(password)
        .isNotice(PostIsNotice.NORMAL)
        .isSecret(PostIsSecret.NORMAL)
        .build();
    Set<String> authorities = new HashSet<>();

    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.findById(request.getPostId())).willReturn(java.util.Optional.of(post));
    given(accountContextService.getCurrentClientIP()).willReturn(ip);
    willDoNothing().given(attachUpdateService).update(post.getPostId(), null, files);
    given(passwordEncoder.matches(getAnonymous().getAnonymousPassword(), password))
        .willReturn(true);
    given(accountContextService.isSignIn()).willReturn(false);
    given(postJpaRepository.save(post)).willReturn(savedPost);

    // when, then
    assertDoesNotThrow(() -> postUpdateService.update(request, null));
  }

  @Test
  void ??????_?????????_??????_??????_?????????() {
    // given
    String ip = "127.0.0.1";
    List<PostCreateDto.TagDto> tagDtoList = Arrays.asList(new TagDto(1L), new TagDto(2L));
    String password = "qwerty";
    Post post = new Post(
        getAnonymous()
        , getBoard()
        , getPostContent()
        , PostIsNotice.NORMAL
        , PostIsSecret.NORMAL
        , new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"))
        , new ArrayList<>()
        , new ArrayList<>()
        , ip);
    PostUpdateDto.Request request = PostUpdateDto.Request
        .builder()
        .postId(1L)
        .postContent(new PostContent("??????????????????", "??????????????????"))
        .anonymousPassword(password)
        .isNotice(PostIsNotice.NORMAL)
        .isSecret(PostIsSecret.NORMAL)
        .tagList(tagDtoList)
        .build();
    Set<String> authorities = new HashSet<>();
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.findById(request.getPostId())).willReturn(java.util.Optional.of(post));

    // when
    BusinessException businessException
        = assertThrows(BusinessException.class, () -> postUpdateService.update(request, files));

    // then
    assertThat(businessException.getErrorCode(), is(TagErrorCode.ANONYMOUS_CAN_NOT_TAG));
    assertThat(businessException.getErrorCode().getMessage(), is("?????? ???????????? ????????? ??? ??? ????????????"));
  }

  @Test
  void ????????????_??????_??????() {
    // given
    String ip = "127.0.0.1";
    Post post = new Post(
        getAccount()
        , getBoard()
        , getPostContent()
        , PostIsNotice.NORMAL
        , PostIsSecret.NORMAL
        , new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"))
        , new ArrayList<>()
        , new ArrayList<>()
        , ip);
    PostUpdateDto.Request request = PostUpdateDto.Request
        .builder()
        .postId(1L)
        .postContent(new PostContent("??????????????????", "??????????????????"))
        .isNotice(PostIsNotice.NORMAL)
        .isSecret(PostIsSecret.NORMAL)
        .tagList(Arrays.asList(new PostCreateDto.TagDto(1L)))
        .build();
    Set<String> authorities = new HashSet<>();

    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.findById(request.getPostId())).willReturn(java.util.Optional.of(post));
    given(accountContextService.isSignIn()).willReturn(true);
    given(tagJpaRepository.findById(anyLong())).willThrow(new BusinessException(TagErrorCode.NO_SUCH_TAG));

    // when
    BusinessException businessException
        = assertThrows(BusinessException.class, () -> postUpdateService.update(request, null));

    // then
    assertThat(businessException.getErrorCode(), is(TagErrorCode.NO_SUCH_TAG));
    assertThat(businessException.getErrorCode().getMessage(), is("???????????? ?????? ??????"));
  }

  @Test
  void ????????????_??????_?????????() {
    // given
    PostUpdateDto.Request request = PostUpdateDto.Request
        .builder()
        .postId(1L)
        .postContent(new PostContent("??????????????????", "??????????????????"))
        .anonymousPassword("password")
        .isNotice(PostIsNotice.NORMAL)
        .isSecret(PostIsSecret.NORMAL)
        .build();

    given(postJpaRepository.findById(request.getPostId()))
        .willThrow(new BusinessException(PostErrorCode.NO_SUCH_POST));

    // when
    BusinessException businessException
        = assertThrows(BusinessException.class, () -> postUpdateService.update(request, null));

    // then
    assertThat(businessException.getErrorCode(), is(PostErrorCode.NO_SUCH_POST));
    assertThat(businessException.getErrorCode().getMessage(), is("???????????? ?????? ?????????"));
  }

  @Test
  void ????????????_??????_?????????() {
    // given
    String ip = "127.0.0.1";
    Post post = new Post(
        getAnonymous()
        , getBoard()
        , getPostContent()
        , PostIsNotice.NORMAL
        , PostIsSecret.NORMAL
        , new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"))
        , new ArrayList<>()
        , new ArrayList<>()
        , ip);
    PostUpdateDto.Request request = PostUpdateDto.Request
        .builder()
        .postId(1L)
        .postContent(new PostContent("??????????????????", "??????????????????"))
        .anonymousPassword(null)
        .isNotice(PostIsNotice.NORMAL)
        .isSecret(PostIsSecret.NORMAL)
        .build();
    Set<String> authorities = new HashSet<>();

    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.findById(request.getPostId())).willReturn(java.util.Optional.of(post));
    given(accountContextService.getCurrentClientIP()).willReturn(ip);
    given(accountContextService.isSignIn()).willReturn(false);

    // when
    BusinessException businessException
        = assertThrows(BusinessException.class, () -> postUpdateService.update(request, null));

    // then
    assertThat(businessException.getErrorCode(), is(PostErrorCode.INVALID_INPUT));
    assertThat(businessException.getErrorCode().getMessage(), is("???????????? ???????????? ??????"));
  }

  @Test
  void ????????????_??????_????????????() {
    // given
    String ip = "127.0.0.1";
    String password = "qwerty";
    Post post = new Post(
        getAnonymous()
        , getBoard()
        , getPostContent()
        , PostIsNotice.NORMAL
        , PostIsSecret.NORMAL
        , new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"))
        , new ArrayList<>()
        , new ArrayList<>()
        , ip);
    PostUpdateDto.Request request = PostUpdateDto.Request
        .builder()
        .postId(1L)
        .postContent(new PostContent("??????????????????", "??????????????????"))
        .anonymousPassword(password)
        .isNotice(PostIsNotice.NORMAL)
        .isSecret(PostIsSecret.NORMAL)
        .build();
    Set<String> authorities = new HashSet<>();

    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.findById(request.getPostId())).willReturn(java.util.Optional.of(post));
    given(accountContextService.getCurrentClientIP()).willReturn(ip);
    given(accountContextService.isSignIn()).willReturn(false);
    given(passwordEncoder.matches(getAnonymous().getAnonymousPassword(), password))
        .willThrow(new BusinessException(PostErrorCode.ANONYMOUS_PASSWORD_INCORRECT));

    // when
    BusinessException businessException
        = assertThrows(BusinessException.class, () -> postUpdateService.update(request, null));

    // then
    assertThat(businessException.getErrorCode(), is(PostErrorCode.ANONYMOUS_PASSWORD_INCORRECT));
    assertThat(businessException.getErrorCode().getMessage(), is("?????? ????????? ??????????????? ???????????????"));
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

  private Anonymous getAnonymous() {
    String anonymousNickName = "??????";
    String anonymousPassword = "qwerty";
    return new Anonymous(anonymousNickName, anonymousPassword);
  }
}
