package com.se.apiserver.v1.post.application.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.post.application.dto.PostCreateDto;
import com.se.apiserver.v1.post.application.dto.PostCreateDto.TagDto;
import com.se.apiserver.v1.post.application.dto.PostUpdateDto;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.domain.exception.NoticeSizeException;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.tag.application.error.TagErrorCode;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import java.lang.reflect.Field;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort.Direction;
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
  @InjectMocks
  private PostUpdateService postUpdateService;

  @Test
  void 회원_게시글_수정_성공() {
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
        .postContent(new PostContent("제목제목제목", "내용내용내용"))
        .isNotice(PostIsNotice.NORMAL)
        .isSecret(PostIsSecret.NORMAL)
        .tagList(Arrays.asList(new PostCreateDto.TagDto(1L)))
        .build();
    Set<String> authorities = new HashSet<>();

    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.findById(request.getPostId())).willReturn(java.util.Optional.of(post));
    given(accountContextService.getCurrentClientIP()).willReturn(ip);
    given(tagJpaRepository.findById(anyLong())).willReturn(java.util.Optional.of(new Tag("태그")));
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getCurrentAccountId()).willReturn(getAccount().getAccountId());
    given(postJpaRepository.save(post)).willReturn(savedPost);

    // when, then
    assertDoesNotThrow(() -> postUpdateService.update(request));
  }

  @Test
  void 익명_사용자_게시글_수정_성공() {
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
        .postContent(new PostContent("제목제목제목", "내용내용내용"))
        .anonymousPassword(password)
        .isNotice(PostIsNotice.NORMAL)
        .isSecret(PostIsSecret.NORMAL)
        .build();
    Set<String> authorities = new HashSet<>();

    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.findById(request.getPostId())).willReturn(java.util.Optional.of(post));
    given(accountContextService.getCurrentClientIP()).willReturn(ip);
    given(passwordEncoder.matches(getAnonymous().getAnonymousPassword(), password))
        .willReturn(true);
    given(accountContextService.isSignIn()).willReturn(false);
    given(postJpaRepository.save(post)).willReturn(savedPost);

    // when, then
    assertDoesNotThrow(() -> postUpdateService.update(request));
  }

  @Test
  void 익명_사용자_태그_사용_불가능() {
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
        .postContent(new PostContent("제목제목제목", "내용내용내용"))
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
        = assertThrows(BusinessException.class, () -> postUpdateService.update(request));

    // then
    assertThat(businessException.getErrorCode(), is(TagErrorCode.ANONYMOUS_CAN_NOT_TAG));
    assertThat(businessException.getErrorCode().getMessage(), is("익명 사용자는 태그를 달 수 없습니다"));
  }

  @Test
  void 존재하지_않는_태그() {
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
        .postContent(new PostContent("제목제목제목", "내용내용내용"))
        .isNotice(PostIsNotice.NORMAL)
        .isSecret(PostIsSecret.NORMAL)
        .tagList(Arrays.asList(new PostCreateDto.TagDto(1L)))
        .build();
    Set<String> authorities = new HashSet<>();

    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.findById(request.getPostId())).willReturn(java.util.Optional.of(post));
    given(accountContextService.isSignIn()).willReturn(true);
    given(tagJpaRepository.findById(anyLong()))
        .willThrow(new BusinessException(TagErrorCode.NO_SUCH_TAG));

    // when
    BusinessException businessException
        = assertThrows(BusinessException.class, () -> postUpdateService.update(request));

    // then
    assertThat(businessException.getErrorCode(), is(TagErrorCode.NO_SUCH_TAG));
    assertThat(businessException.getErrorCode().getMessage(), is("존재하지 않는 태그"));
  }

  @Test
  void 존재하지_않는_게시글() {
    // given
    PostUpdateDto.Request request = PostUpdateDto.Request
        .builder()
        .postId(1L)
        .postContent(new PostContent("제목제목제목", "내용내용내용"))
        .anonymousPassword("password")
        .isNotice(PostIsNotice.NORMAL)
        .isSecret(PostIsSecret.NORMAL)
        .build();

    given(postJpaRepository.findById(request.getPostId()))
        .willThrow(new BusinessException(PostErrorCode.NO_SUCH_POST));

    // when
    BusinessException businessException
        = assertThrows(BusinessException.class, () -> postUpdateService.update(request));

    // then
    assertThat(businessException.getErrorCode(), is(PostErrorCode.NO_SUCH_POST));
    assertThat(businessException.getErrorCode().getMessage(), is("존재하지 않는 게시글"));
  }

  @Test
  void 올바르지_않은_입력값() {
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
        .postContent(new PostContent("제목제목제목", "내용내용내용"))
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
        = assertThrows(BusinessException.class, () -> postUpdateService.update(request));

    // then
    assertThat(businessException.getErrorCode(), is(PostErrorCode.INVALID_INPUT));
    assertThat(businessException.getErrorCode().getMessage(), is("입력값이 올바르지 않음"));
  }

  @Test
  void 올바르지_않은_비밀번호() {
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
        .postContent(new PostContent("제목제목제목", "내용내용내용"))
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
        = assertThrows(BusinessException.class, () -> postUpdateService.update(request));

    // then
    assertThat(businessException.getErrorCode(), is(PostErrorCode.ANONYMOUS_PASSWORD_INCORRECT));
    assertThat(businessException.getErrorCode().getMessage(), is("익명 게시글 비밀번호가 틀렸습니다"));
  }

  @Test
  void 관리자가_일반_게시글을_공지_게시글로_수정_성공() throws NoSuchFieldException, IllegalAccessException {
    // given
    String ip = "127.0.0.1";
    Set<String> authorities = Set.of("MENU_MANAGE");
    Post post = new Post(
        getAssistantAccount()
        , getBoard()
        , getPostContent()
        , PostIsNotice.NORMAL
        , PostIsSecret.NORMAL
        , new HashSet<>(authorities)
        , new ArrayList<>()
        , new ArrayList<>()
        , ip);
    Post savedPost = post;
    PostUpdateDto.Request request = PostUpdateDto.Request
        .builder()
        .postId(1L)
        .postContent(new PostContent("제목제목제목", "내용내용내용"))
        .isNotice(PostIsNotice.NOTICE)
        .isSecret(PostIsSecret.NORMAL)
        .build();
    List<Post> postList = Arrays.asList(post);
    Page<Post> allByBoard = new PageImpl<>(postList);
    Field field = postUpdateService.getClass().getDeclaredField("MAX_NOTICE_SIZE");
    field.setAccessible(true);
    field.set(postUpdateService, 10);

    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.findById(request.getPostId())).willReturn(java.util.Optional.of(post));
    given(postJpaRepository
        .findAllByBoardAndIsNotice(
            new PageRequest(0, (Integer) field.get(postUpdateService), Direction.ASC).of(),
            post.getBoard(),
            request.getIsNotice())).willReturn(allByBoard);
    given(accountContextService.getCurrentClientIP()).willReturn(ip);
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getCurrentAccountId()).willReturn(getAccount().getAccountId());
    given(postJpaRepository.save(post)).willReturn(savedPost);

    // when, then
    assertDoesNotThrow(() -> postUpdateService.update(request));
  }

  @Test
  void 공지_개수_제한_초과로_인한_공지글_등록_실패() throws NoSuchFieldException, IllegalAccessException {
    // given
    Set<String> authorities = Set.of("MENU_MANAGE");
    Post post = new Post(
        getAssistantAccount()
        , getBoard()
        , getPostContent()
        , PostIsNotice.NORMAL
        , PostIsSecret.NORMAL
        , new HashSet<>(authorities)
        , new ArrayList<>()
        , new ArrayList<>()
        , "127.0.0.1");
    PostUpdateDto.Request request = PostUpdateDto.Request
        .builder()
        .postId(1L)
        .postContent(new PostContent("제목제목제목", "내용내용내용"))
        .isNotice(PostIsNotice.NOTICE)
        .isSecret(PostIsSecret.NORMAL)
        .build();
    List<Post> postList = Arrays.asList(post);
    Page<Post> allByBoard = new PageImpl<>(postList);
    Field field = postUpdateService.getClass().getDeclaredField("MAX_NOTICE_SIZE");
    field.setAccessible(true);
    field.set(postUpdateService, 1);

    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.findById(request.getPostId())).willReturn(java.util.Optional.of(post));
    given(postJpaRepository
        .findAllByBoardAndIsNotice(
            new PageRequest(0, (Integer) field.get(postUpdateService), Direction.ASC).of(),
            post.getBoard(),
            request.getIsNotice())).willReturn(allByBoard);

    // when
    NoticeSizeException noticeSizeException
        = assertThrows(NoticeSizeException.class, () -> postUpdateService.update(request));

    // then
    assertThat(noticeSizeException.getMessage(), is("더 이상 공지글을 등록할 수 없습니다."));
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

  private Account getAccount() {
    return new Account(1L
        , "studentId"
        , "1234"
        , "name"
        , "student"
        , "studentId"
        , AccountType.STUDENT
        , "01012345678"
        , "se@kumoh.ac.kr"
        , "127.0.0.1"
        , InformationOpenAgree.AGREE
        , null
        , null);
  }

  private Account getAssistantAccount() {
    return new Account(1L
        , "assistantId"
        , "1234"
        , "name"
        , "assistant"
        , "assistantId"
        , AccountType.ASSISTANT
        , "01012345678"
        , "se@kumoh.ac.kr"
        , "127.0.0.1"
        , InformationOpenAgree.AGREE
        , null
        , null);
  }

  private Anonymous getAnonymous() {
    String anonymousNickName = "ㅇㅇ";
    String anonymousPassword = "qwerty";
    return new Anonymous(anonymousNickName, anonymousPassword);
  }
}
