package com.se.apiserver.v1.attach.application.service;

import static com.mysema.commons.lang.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.attach.application.dto.AttachReadDto;
import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.dto.MultipartFileUploadDto;
import com.se.apiserver.v1.multipartfile.application.service.MultipartFileUploadService;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsSecret;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import com.se.apiserver.v1.tag.domain.entity.Tag;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class AttachCreateServiceTest {

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
      new MockMultipartFile("file"
          , "file.png"
          , "text/plain"
          , data.getBytes(StandardCharsets.UTF_8)),
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
  private MultipartFileUploadService multipartFileUploadService;

  @Mock
  private AttachJpaRepository attachJpaRepository;

  @Mock
  private PostJpaRepository postJpaRepository;

  @Mock
  private ReplyJpaRepository replyJpaRepository;

  @InjectMocks
  private AttachCreateService attachCreateService;

  @Test
  void 게시글_첨부_파일_등록_성공() {
    // given
    Long postId = 1L;
    Post post = getPost();
    List<MultipartFileUploadDto> multipartFileUploadDtoList = Arrays.asList(
        new MultipartFileUploadDto("URL1", "FileName1"),
        new MultipartFileUploadDto("URL2", "FileName2"),
        new MultipartFileUploadDto("URL3", "FileName3"),
        new MultipartFileUploadDto("URL4", "FileName4"),
        new MultipartFileUploadDto("URL5", "FileName5")
    );
    List<Attach> attaches = Arrays.asList(
        new Attach("URL1", "FileName1", post),
        new Attach("URL2", "FileName2", post),
        new Attach("URL3", "FileName3", post),
        new Attach("URL4", "FileName4", post),
        new Attach("URL5", "FileName5", post)
    );

    given(postJpaRepository.findById(postId)).willReturn(java.util.Optional.of(post));
    given(multipartFileUploadService.upload(files)).willReturn(multipartFileUploadDtoList);
    given(attachJpaRepository.saveAll(any(List.class))).willReturn(attaches);

    // when
    List<AttachReadDto.Response> responseList = attachCreateService.create(postId, null, files);

    // then
    assertThat(responseList.size(), is(multipartFileUploadDtoList.size()));
  }

  @Test
  void 댓글_첨부파일_등록_성공() {
    // given
    Long replyId = 1L;
    Post post = getPost();
    Reply reply = getReply();

    List<MultipartFileUploadDto> multipartFileUploadDtoList = Arrays.asList(
        new MultipartFileUploadDto("URL1", "FileName1"),
        new MultipartFileUploadDto("URL2", "FileName2"),
        new MultipartFileUploadDto("URL3", "FileName3"),
        new MultipartFileUploadDto("URL4", "FileName4"),
        new MultipartFileUploadDto("URL5", "FileName5")
    );
    List<Attach> attaches = Arrays.asList(
        new Attach("URL1", "FileName1", post),
        new Attach("URL2", "FileName2", post),
        new Attach("URL3", "FileName3", post),
        new Attach("URL4", "FileName4", post),
        new Attach("URL5", "FileName5", post)
    );

    given(replyJpaRepository.findById(replyId)).willReturn(java.util.Optional.of(reply));
    given(multipartFileUploadService.upload(files)).willReturn(multipartFileUploadDtoList);
    given(attachJpaRepository.saveAll(any(List.class))).willReturn(attaches);

    // when
    List<AttachReadDto.Response> responseList = attachCreateService.create(null, replyId, files);

    // then
    assertThat(responseList.size(), is(multipartFileUploadDtoList.size()));
  }

  @Test
  void 존재하지_않는_게시글() {
    // given
    Long postId = 1L;
    given(postJpaRepository.findById(postId))
        .willThrow(new BusinessException(PostErrorCode.NO_SUCH_POST));

    // when
    BusinessException businessException
        = assertThrows(BusinessException.class,
        () -> attachCreateService.create(postId, null, files));

    // then
    assertThat(businessException.getErrorCode(), is(PostErrorCode.NO_SUCH_POST));
    assertThat(businessException.getMessage(), is("존재하지 않는 게시글"));
  }

  @Test
  void 존재하지_않는_댓글() {
    // given
    Long replyId = 1L;
    given(replyJpaRepository.findById(replyId))
        .willThrow(new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));

    // when
    BusinessException businessException = assertThrows(BusinessException.class,
        () -> attachCreateService.create(null, replyId, files));

    // then
    assertThat(businessException.getErrorCode(), is(ReplyErrorCode.NO_SUCH_REPLY));
    assertThat(businessException.getMessage(), is("존재하지 않는 댓글"));
  }

  @Test
  void 올바르지_않은_입력_값() {
    // given
    Long postId = 1L;
    Long replyId = 1L;

    // when
    BusinessException businessException = assertThrows(BusinessException.class
        , () -> attachCreateService.create(postId, replyId, files));

    // then
    assertThat(businessException.getErrorCode(), is(AttachErrorCode.INVALID_INPUT));
    assertThat(businessException.getMessage(), is("입력 값이 올바르지 않음"));
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

  private Reply getReply() {
    return new Reply(getPost()
        , "=====마감====="
        , ReplyIsSecret.SECRET
        , null
        , null
        , "127.0.0.1"
        , getAccount());
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
