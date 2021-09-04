package com.se.apiserver.v1.post.application.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.notice.application.service.NoticeSendService;
import com.se.apiserver.v1.post.application.dto.PostCreateDto;
import com.se.apiserver.v1.post.application.dto.PostCreateDto.TagDto;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.tag.application.error.TagErrorCode;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class PostCreateServiceTest {

  @Mock
  private PostJpaRepository postJpaRepository;
  @Mock
  private AccountContextService accountContextService;
  @Mock
  private BoardJpaRepository boardJpaRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private TagJpaRepository tagJpaRepository;
  @Mock
  private AttachJpaRepository attachJpaRepository;
  @Mock
  private NoticeSendService noticeSendService;
  @InjectMocks
  private PostCreateService postCreateService;

  @Test
  void 회원_게시글_등록_성공() {
    // given
    Long boardId = 1L;
    String ip = "127.0.0.1";
    Set<String> authorities = Set.of("MENU_MANAGE");
    Account account = getAccount();
    Board board = getBoard();
    List<Tag> tags = Arrays.asList(new Tag("태그"));
    List<TagDto> tagDtoList = Arrays.asList(new TagDto(1L));
    Attach attach = new Attach("URL", "FILENAME");
    List<Attach> attaches = Arrays.asList(attach);
    Post post = new Post(account, board, getPostContent(), PostIsNotice.NORMAL, PostIsSecret.NORMAL,
        authorities, tags, attaches, "127.0.0.1");
    PostCreateDto.Request request = PostCreateDto.Request
        .builder().boardId(boardId)
        .postContent(post.getPostContent())
        .isSecret(PostIsSecret.NORMAL)
        .isNotice(PostIsNotice.NORMAL)
        .tagList(tagDtoList)
        .build();

    given(boardJpaRepository.findById(boardId)).willReturn(java.util.Optional.of(board));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(accountContextService.getCurrentClientIP()).willReturn(ip);
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getContextAccount()).willReturn(account);
    given(tagJpaRepository.findById(tagDtoList.get(0).getTagId()))
        .willReturn(java.util.Optional.ofNullable(tags.get(0)));
    given(postJpaRepository.save(any(Post.class))).willReturn(post);

    // when, then
    assertDoesNotThrow(() -> postCreateService.create(request));
  }

  @Test
  void 익명_사용자_게시글_작성_성공() {
    // given
    Long boardId = 1L;
    String ip = "127.0.0.1";
    Set<String> authorities = Set.of("FREEBOARD_ACCESS");
    Anonymous anonymous = getAnonymous();
    Board board = getBoard();
    Attach attach = new Attach("URL", "FILENAME");
    List<Attach> attaches = Arrays.asList(attach);
    Post post = new Post(anonymous, board, getPostContent(), PostIsNotice.NORMAL,
        PostIsSecret.NORMAL,
        authorities, new ArrayList<>(), attaches, "127.0.0.1");
    PostCreateDto.Request request = PostCreateDto.Request
        .builder().boardId(boardId)
        .postContent(post.getPostContent())
        .isSecret(PostIsSecret.NORMAL)
        .isNotice(PostIsNotice.NORMAL)
        .anonymous(anonymous)
        .build();

    given(boardJpaRepository.findById(boardId)).willReturn(java.util.Optional.of(board));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(accountContextService.getCurrentClientIP()).willReturn(ip);
    given(accountContextService.isSignIn()).willReturn(false);
    given(passwordEncoder.encode(request.getAnonymous().getAnonymousPassword()))
        .willReturn("password");
    given(postJpaRepository.save(any(Post.class))).willReturn(post);

    // when, then
    assertDoesNotThrow(() -> postCreateService.create(request));
  }

  @Test
  void 존재하지_않는_게시판() {
    // given
    Long boardId = 2L;
    MultipartFile[] files = new MultipartFile[1];
    PostCreateDto.Request request = PostCreateDto.Request
        .builder().boardId(boardId)
        .postContent(getPostContent())
        .isSecret(PostIsSecret.NORMAL)
        .isNotice(PostIsNotice.NORMAL)
        .anonymous(getAnonymous())
        .build();

    given(boardJpaRepository.findById(boardId))
        .willThrow(new BusinessException(BoardErrorCode.NO_SUCH_BOARD));
    // when
    BusinessException businessException = assertThrows(BusinessException.class,
        () -> postCreateService.create(request));

    // then
    assertThat(businessException.getErrorCode(), is(BoardErrorCode.NO_SUCH_BOARD));
    assertThat(businessException.getMessage(), is("존재하지 않는 게시판"));
  }

  @Test
  void 익명_사용자가_태그_등록_실패() {
    // given
    Long boardId = 2L;
    Board board = getBoard();
    MultipartFile[] files = new MultipartFile[1];
    List<TagDto> tagDtoList = Arrays.asList(new TagDto(1L));
    PostCreateDto.Request request = PostCreateDto.Request
        .builder().boardId(boardId)
        .postContent(getPostContent())
        .isSecret(PostIsSecret.NORMAL)
        .anonymous(getAnonymous())
        .tagList(tagDtoList)
        .build();

    given(boardJpaRepository.findById(boardId)).willReturn(java.util.Optional.of(board));
    given(accountContextService.isSignIn()).willReturn(false);

    // when
    BusinessException businessException = assertThrows(BusinessException.class,
        () -> postCreateService.create(request));

    // then
    assertThat(businessException.getErrorCode(), is(TagErrorCode.ANONYMOUS_CAN_NOT_TAG));
    assertThat(businessException.getMessage(), is("익명 사용자는 태그를 달 수 없습니다"));
  }

  @Test
  void 접근_권한_없음() {
    // given
    Long boardId = 2L;
    Board board = getBoard();
    MultipartFile[] files = new MultipartFile[1];
    PostCreateDto.Request request = PostCreateDto.Request
        .builder().boardId(boardId)
        .postContent(getPostContent())
        .isSecret(PostIsSecret.NORMAL)
        .anonymous(getAnonymous())
        .tagList(new ArrayList<>())
        .build();
    Set<String> authorities = new HashSet<>();
    given(boardJpaRepository.findById(boardId)).willReturn(java.util.Optional.of(board));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);

    // when
    AccessDeniedException accessDeniedException = assertThrows(AccessDeniedException.class,
        () -> postCreateService.create(request));

    // then
    assertThat(accessDeniedException.getMessage(), is("접근 권한이 없습니다"));
  }

  private Anonymous getAnonymous() {
    String anonymousNickName = "ㅇㅇ";
    String anonymousPassword = "qwerty";
    return new Anonymous(anonymousNickName, anonymousPassword);
  }

  @Test
  void 관리자가_아닌_사용자가_공지글_등록_실패() {
    // given
    Long boardId = 1L;
    String ip = "127.0.0.1";
    Set<String> authorities = Set.of("FREEBOARD_ACCESS");
    Account account = getAccount();
    Board board = getBoard();
    Attach attach = new Attach("URL", "FILENAME");
    List<Attach> attaches = Arrays.asList(attach);
    MultipartFile[] files = new MultipartFile[1];
    PostCreateDto.Request request = PostCreateDto.Request
        .builder().boardId(boardId)
        .postContent(getPostContent())
        .isSecret(PostIsSecret.NORMAL)
        .isNotice(PostIsNotice.NOTICE)
        .build();

    given(boardJpaRepository.findById(boardId)).willReturn(java.util.Optional.of(board));
    given(accountContextService.getContextAuthorities()).willReturn(authorities);
    given(accountContextService.getCurrentClientIP()).willReturn(ip);
    given(accountContextService.isSignIn()).willReturn(true);
    given(accountContextService.getContextAccount()).willReturn(account);

    // when
    BusinessException businessException = assertThrows(BusinessException.class,
        () -> postCreateService.create(request));

    // then
    assertThat(businessException.getErrorCode(), is(PostErrorCode.ONLY_ADMIN_SET_NOTICE));
    assertThat(businessException.getMessage(), is("관리자만 공지글 설정 가능합니다"));
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
