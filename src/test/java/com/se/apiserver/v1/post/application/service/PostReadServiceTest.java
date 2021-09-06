package com.se.apiserver.v1.post.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.dto.PostDeleteDto;
import com.se.apiserver.v1.post.application.dto.PostDeleteDto.AnonymousPostDeleteRequest;
import com.se.apiserver.v1.post.application.dto.PostReadDto;
import com.se.apiserver.v1.post.application.dto.PostReadDto.PostSearchRequest;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.application.error.PostSearchErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.domain.entity.PostSearchType;
import com.se.apiserver.v1.post.domain.repository.PostRepositoryProtocol;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostQueryRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class PostReadServiceTest {

  private final int TUPLE_COUNT = 10;
  @Mock
  private PostJpaRepository postJpaRepository;
  @Mock
  private AccountContextService accountContextService;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private BoardJpaRepository boardJpaRepository;
  @Mock
  private PostQueryRepository postQueryRepository;
  @Mock
  private PostRepositoryProtocol postRepositoryProtocol;
  @InjectMocks
  private PostReadService postReadService;

  @Test
  void 단일_게시글_조회_성공() {
    // given
    Long postId = 1L;
    Set<String> authorities = new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"));
    List<Tag> tags = new ArrayList<>();
    Post post = new Post(getAccount()
        , getBoard()
        , getPostContent()
        , PostIsNotice.NORMAL
        , PostIsSecret.NORMAL
        , authorities
        , tags
        , null
        , "127.0.0.1");

    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.save(post)).willReturn(post);

    // when
    PostReadDto.Response response = postReadService.read(postId);

    // then
    assertThat(response.getPostId(), is(post.getPostId()));
    assertThat(response.getBoardId(), is(getBoard().getBoardId()));
    assertThat(response.getBoardNameEng(), is(getBoard().getNameEng()));
    assertThat(response.getBoardNameKor(), is(getBoard().getNameKor()));
    assertThat(response.getViews(), is(1));
    assertThat(response.getIsSecret(), is(PostIsSecret.NORMAL));
    assertThat(response.getIsNotice(), is(PostIsNotice.NORMAL));
    assertThat(response.getNickname(), is(getAccount().getNickname()));
    assertThat(response.getAccountType(), is(AccountType.STUDENT));
    assertThat(response.getPostContent().getTitle(), is(getPostContent().getTitle()));
    assertThat(response.getPostContent().getText(), is(getPostContent().getText()));
  }

  @Test
  void 비밀_게시글_조회_성공() {
    // given
    Long postId = 1L;
    String password = "qwerty";
    List<Tag> tags = new ArrayList<>();
    Post post = new Post(getAnonymous()
        , getBoard()
        , getPostContent()
        , PostIsNotice.NORMAL
        , PostIsSecret.NORMAL
        , new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"))
        , tags
        , null
        , "127.0.0.1");

    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));
    given(postJpaRepository.save(post)).willReturn(post);
    given(passwordEncoder.matches(password, post.getAnonymousPassword())).willReturn(true);

    // when
    PostReadDto.Response response = postReadService.readAnonymousSecretPost(postId, password);

    // then
    assertThat(response.getPostId(), is(post.getPostId()));
    assertThat(response.getBoardId(), is(getBoard().getBoardId()));
    assertThat(response.getBoardNameEng(), is(getBoard().getNameEng()));
    assertThat(response.getBoardNameKor(), is(getBoard().getNameKor()));
    assertThat(response.getViews(), is(1));
    assertThat(response.getIsSecret(), is(PostIsSecret.NORMAL));
    assertThat(response.getIsNotice(), is(PostIsNotice.NORMAL));
    assertThat(response.getNickname(), is(getAnonymous().getAnonymousNickname()));
    assertThat(response.getAccountType(), is(AccountType.ANONYMOUS));
    assertThat(response.getPostContent().getTitle(), is(getPostContent().getTitle()));
    assertThat(response.getPostContent().getText(), is(getPostContent().getText()));
  }

  @Test
  void 비밀번호_불일치로_비밀_게시글_조회_실패() {
    // given
    Long postId = 1L;
    String password = "wrong";
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
    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));
    given(passwordEncoder.matches(password, post.getAnonymousPassword())).willReturn(false);

    // when
    BusinessException businessException
        = assertThrows(BusinessException.class,
        () -> postReadService.readAnonymousSecretPost(postId, password));

    // then
    assertThat(businessException.getErrorCode(), is(PostErrorCode.ANONYMOUS_PASSWORD_INCORRECT));
    assertThat(businessException.getMessage(), is("익명 게시글 비밀번호가 틀렸습니다"));
  }

  @Test
  void 존재하지_않는_게시글() {
    // given
    Long postId = 1L;
    given(postJpaRepository.findById(postId))
        .willThrow(new BusinessException(PostErrorCode.NO_SUCH_POST));

    // when
    BusinessException businessException
        = assertThrows(BusinessException.class, () -> postReadService.read(postId));

    // then
    assertThat(businessException.getErrorCode(), is(PostErrorCode.NO_SUCH_POST));
    assertThat(businessException.getMessage(), is("존재하지 않는 게시글"));
  }

  @Test
  void 삭제된_게시글() {
    // given
    Long postId = 1L;
    Set<String> authorities = new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"));
    List<Tag> tags = new ArrayList<>();
    Post post = new Post(getAccount()
        , getBoard()
        , getPostContent()
        , PostIsNotice.NORMAL
        , PostIsSecret.NORMAL
        , authorities
        , tags
        , null
        , "127.0.0.1");
    post.delete();

    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));

    // when
    BusinessException businessException
        = assertThrows(BusinessException.class, () -> postReadService.read(postId));

    // then
    assertThat(businessException.getErrorCode(), is(PostErrorCode.DELETED_POST));
    assertThat(businessException.getMessage(), is("삭제된 게시글입니다"));
  }

  @Test
  void 게시글_접근_권한_없음() {
    // given
    Long postId = 1L;
    Set<String> authorities = new HashSet<>();
    List<Tag> tags = new ArrayList<>();
    Post post = new Post(getAccount(), getBoard(), getPostContent(), PostIsNotice.NORMAL,
        PostIsSecret.NORMAL,
        new HashSet<>(Arrays.asList("FREEBOARD_ACCESS")), tags, null, "127.0.0.1");

    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);

    // when
    AccessDeniedException accessDeniedException
        = assertThrows(AccessDeniedException.class, () -> postReadService.read(postId));

    // then
    assertThat(accessDeniedException.getMessage(), is("접근 권한이 없습니다"));
  }

  @Test
  void 게시글_목록_조회() {
    // given
    Long boardId = 1L;
    Board board = getBoard();
    Set<String> authorities = new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"));
    List<Post> postList = new ArrayList<>();
    Pageable pageable = PageRequest.of(0, 10, Direction.ASC, "postId");
    for (int i = 0; i < TUPLE_COUNT; i++) {
      PostContent postContent = new PostContent(Integer.toString(i), "text" + i);
      List<Tag> tags = new ArrayList<>();

      Post post = new Post(getAccount()
          , board
          , postContent
          , PostIsNotice.NORMAL
          , PostIsSecret.NORMAL
          , authorities
          , tags
          , null
          , "127.0.0.1");
      postList.add(post);
    }
    Page<Post> postPage = new PageImpl<>(postList);

    given(boardJpaRepository.findById(boardId)).willReturn(java.util.Optional.of(board));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postJpaRepository.findAllByBoard(board, pageable)).willReturn(postPage);

    // when
    PostReadDto.PostListResponse postListResponse
        = postReadService.readBoardPostList(pageable, boardId);

    // then
    assertThat(postListResponse.getPostListItem().getSize(), is(TUPLE_COUNT));
    assertThat(postListResponse.getBoardId(), is(board.getBoardId()));
    assertThat(postListResponse.getBoardNameEng(), is(board.getNameEng()));
    assertThat(postListResponse.getBoardNameKor(), is(board.getNameKor()));
  }

  @Test
  void 게시글_검색_성공() {
    // given
    Long boardId = 1L;
    Board board = getBoard();
    Set<String> authorities = new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"));
    List<Post> postList = new ArrayList<>();
    for (int i = 0; i < TUPLE_COUNT; i++) {
      PostContent postContent
          = new PostContent("제목제목제목", "내용내용내용");
      List<Tag> tags = new ArrayList<>();

      Post post = new Post(getAccount()
          , board
          , postContent
          , PostIsNotice.NORMAL
          , PostIsSecret.NORMAL
          , authorities
          , tags
          , null
          , "127.0.0.1");
      postList.add(post);
    }
    Page<Post> postPage = new PageImpl<>(postList);
    PostSearchRequest postSearchRequest = PostSearchRequest
        .builder()
        .boardId(boardId)
        .keyword("제목")
        .postSearchType(PostSearchType.TITLE_TEXT)
        .pageRequest(com.se.apiserver.v1.common.infra.dto.PageRequest
            .builder()
            .direction(Direction.ASC)
            .page(1)
            .size(10)
            .build())
        .build();

    given(boardJpaRepository.findById(boardId)).willReturn(java.util.Optional.of(board));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postQueryRepository.search(postSearchRequest)).willReturn(postPage);

    // when
    PostReadDto.PostListResponse postListResponse
        = postReadService.search(postSearchRequest);

    // then
    assertThat(postListResponse.getPostListItem().getSize(), is(TUPLE_COUNT));
  }

  @Test
  void 게시글_검색_오류() {
    // given
    Long boardId = 1L;
    Board board = getBoard();
    Set<String> authorities = new HashSet<>(Arrays.asList("FREEBOARD_ACCESS"));
    List<Post> postList = new ArrayList<>();
    for (int i = 0; i < TUPLE_COUNT; i++) {
      PostContent postContent
          = new PostContent("제목제목제목", "내용내용내용");
      List<Tag> tags = new ArrayList<>();

      Post post = new Post(getAccount()
          , board
          , postContent
          , PostIsNotice.NORMAL
          , PostIsSecret.NORMAL
          , authorities
          , tags
          , null
          , "127.0.0.1");
      postList.add(post);
    }
    Page<Post> postPage = new PageImpl<>(postList);
    PostSearchRequest postSearchRequest = PostSearchRequest
        .builder()
        .boardId(boardId)
        .keyword("제목")
        .postSearchType(null)
        .pageRequest(com.se.apiserver.v1.common.infra.dto.PageRequest
            .builder()
            .direction(Direction.ASC)
            .page(1)
            .size(10)
            .build())
        .build();

    given(boardJpaRepository.findById(boardId)).willReturn(java.util.Optional.of(board));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(postQueryRepository.search(postSearchRequest)).willThrow(new BusinessException(
        PostSearchErrorCode.NO_SUCH_SEARCH_TYPE));

    // when
    BusinessException businessException = assertThrows(BusinessException.class, () -> postReadService.search(postSearchRequest));

    // then
    assertThat(businessException.getErrorCode(), is(PostSearchErrorCode.NO_SUCH_SEARCH_TYPE));
    assertThat(businessException.getMessage(), is("존재하지 않는 검색 타입"));
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
}
