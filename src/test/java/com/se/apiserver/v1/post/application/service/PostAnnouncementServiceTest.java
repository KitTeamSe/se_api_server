package com.se.apiserver.v1.post.application.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.NotFoundException;
import com.se.apiserver.v1.post.application.dto.PostAnnouncementDto;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.domain.repository.PostRepositoryProtocol;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PostAnnouncementServiceTest {

  private final int TUPLE_COUNT = 10;

  @Mock
  private PostRepositoryProtocol postRepositoryProtocol;

  @Mock
  private BoardJpaRepository boardJpaRepository;

  @InjectMocks
  private PostReadService postReadService;

  @Test
  void 게시판_공지_조회_성공() {
    // given
    List<Post> postList = new ArrayList<>();
    Board board = new Board("freeBoard", "SE 게시판");
    Pageable pageable = PageRequest.of(0, 10, Direction.ASC, "postId");
    Account account = new Account(1L, "duckling", "1234", "윤진", "duck", "20180764",
        AccountType.STUDENT, "01097762378", "20180764@kumoh.ac.kr", "127.0.0.1",
        InformationOpenAgree.AGREE, null, null);

    for (int i = 0; i < TUPLE_COUNT; i++) {
      PostContent postContent = new PostContent(Integer.toString(i), "text" + i);
      Set<String> authorities = Set.of("MENU_MANAGE");
      List<Tag> tags = new ArrayList<>();

      Post post = new Post(account, board, postContent, PostIsNotice.NOTICE, PostIsSecret.NORMAL,
          authorities, tags, null, "127.0.0.1");
      postList.add(post);
    }
    Page<Post> postPage = new PageImpl<>(postList);

    given(boardJpaRepository.findById(1L)).willReturn(java.util.Optional.of(board));
    given(postRepositoryProtocol
        .findAllByBoardAndIsNoticeEquals(board, PostIsNotice.NOTICE, pageable))
        .willReturn(postPage);

    // when
    Page<PostAnnouncementDto> dtoPage = postReadService.readAnnouncementList(pageable, 1L);

    // then
    List<PostAnnouncementDto> dtoList = dtoPage.getContent();
    assertThat(dtoList.size(), is(TUPLE_COUNT));
  }

  @Test
  void 존재하지_않는_게시판() {
    // given
    Board board = new Board("freeBoard", "SE 게시판");
    given(boardJpaRepository.findById(1L)).willReturn(java.util.Optional.of(board));

    try {
      // when
      boardJpaRepository.findById(2L);
    } catch (NotFoundException e) {
      // then
      assertThat(e.getMessage(), is("존재하지 않는 게시판입니다."));
    }
  }
}
