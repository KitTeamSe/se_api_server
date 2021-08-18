package com.se.apiserver.v1.board.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.board.application.dto.BoardDeleteDto;
import com.se.apiserver.v1.board.application.dto.BoardDeleteDto.Request;

import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Optional;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsDeleted;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BoardDeleteServiceTest {

  @Mock
  private BoardJpaRepository boardJpaRepository;

  @Mock
  private PostJpaRepository postJpaRepository;

  @Mock
  private AccountContextService accountContextService;

  @InjectMocks
  private BoardDeleteService boardDeleteService;

  private Request createBoardDeleteRequest(){
    Request boardDeleteRequest = Request.builder().boardId(1L).build();

    return boardDeleteRequest;
  }

  private Account createAccount(){
    Account account = new Account(1L, "duckling", "1234", "윤진", "duck", "20180764",
        AccountType.STUDENT, "01097762378", "20180764@kumoh.ac.kr", "127.0.0.1",
        InformationOpenAgree.AGREE, null, null);

    return account;
  }

  private Post createPost(Board board, Account account){
    PostContent postContent = new PostContent("게시글 제목", "게시글 내용");
    Set<String> authorities = Set.of("MENU_MANAGE");
    List<Tag> tags = new ArrayList<>();

    Post post = new Post(account, board, postContent, PostIsNotice.NOTICE, PostIsSecret.NORMAL,
        authorities, tags, null, "127.0.0.1");

    return post;
  }

  @Test
  public void 게시판_삭제_성공(){
    //Given
    BoardDeleteDto.Request request = createBoardDeleteRequest();
    Board board = new Board("freeboard","자유게시판");
    Board deletedBoard = new Board("deletedBoard","삭제된 게시판");

    Long fakeBoardId = 1L;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    Long fakeDeletedBoardId = 2L;
    ReflectionTestUtils.setField(deletedBoard,"boardId", fakeDeletedBoardId);

    Account account = createAccount();
    Set<String> authorities = Set.of("MENU_MANAGE");

    Post post = createPost(board, account);
    List<Post> postList = new ArrayList<>();
    postList.add(post);
    Long fakePostId = 1L;
    ReflectionTestUtils.setField(post,"postId", fakePostId);

    when(boardJpaRepository.findById(fakeBoardId)).thenReturn(Optional.ofNullable(board));
    when(boardJpaRepository.findByNameEng("deletedBoard")).thenReturn(Optional.ofNullable(deletedBoard));
    when(postJpaRepository.findAllByBoard(board)).thenReturn(postList);
    when(postJpaRepository.findById(fakePostId)).thenReturn(Optional.of(post));
    when(accountContextService.getContextAuthorities()).thenReturn(authorities);

    //When
    boolean result =  boardDeleteService.delete(request.getBoardId());

    Optional<Post> findPost = postJpaRepository.findById(fakePostId);
    assertThat(result).isEqualTo(true);
    assertThat(findPost.get().getBoard().getNameEng()).isEqualTo(deletedBoard.getNameEng());
    assertThat(findPost.get().getPostIsDeleted()).isEqualTo(PostIsDeleted.DELETED);
  }

  @Test
  public void 게시판_삭제_실패_존재하지_않는_게시판() {
    //Given
    BoardDeleteDto.Request request = createBoardDeleteRequest();

    Set<String> authorities = Set.of("MENU_MANAGE");

    when(accountContextService.getContextAuthorities()).thenReturn(authorities);

    //When
    BusinessException exception = assertThrows(BusinessException.class,
        () -> boardDeleteService.delete(request.getBoardId()));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.NO_SUCH_BOARD);
  }

  @Test
  public void 게시판_삭제_실패_게시글_삭제_권한_없음(){
    //Given
    BoardDeleteDto.Request request = createBoardDeleteRequest();
    Board board = new Board("freeboard","자유게시판");
    Board deletedBoard = new Board("deletedBoard","삭제된 게시판");

    Long fakeBoardId = 1L;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    Long fakeDeletedBoardId = 2L;
    ReflectionTestUtils.setField(deletedBoard,"boardId", fakeDeletedBoardId);

    Set<String> authorities = Set.of("");

    Account account = createAccount();
    Post post = createPost(board, account);
    List<Post> postList = new ArrayList<>();
    postList.add(post);
    Long fakePostId = 1L;
    ReflectionTestUtils.setField(post,"postId", fakePostId);


    when(accountContextService.getContextAuthorities()).thenReturn(authorities);
    when(boardJpaRepository.findById(fakeBoardId)).thenReturn(Optional.ofNullable(board));
    when(boardJpaRepository.findByNameEng("deletedBoard")).thenReturn(Optional.ofNullable(deletedBoard));
    when(postJpaRepository.findAllByBoard(board)).thenReturn(postList);

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardDeleteService.delete(request.getBoardId()));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(AccountErrorCode.CAN_NOT_ACCESS_ACCOUNT);
  }

}