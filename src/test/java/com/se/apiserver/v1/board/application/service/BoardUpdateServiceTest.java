package com.se.apiserver.v1.board.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.board.application.dto.BoardUpdateDto;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BoardUpdateServiceTest {

  @Mock
  public BoardJpaRepository boardJpaRepository;

  @Mock
  private MenuJpaRepository menuJpaRepository;

  @Mock
  private AuthorityJpaRepository authorityJpaRepository;

  @InjectMocks
  private BoardUpdateService boardUpdateService;

  private BoardUpdateDto.Request createBoardUpdateRequest(){
    BoardUpdateDto.Request boardUpdateDtoRequest = BoardUpdateDto.Request.builder()
        .boardId(1L)
        .nameEng("freeboard")
        .nameKor("자유게시판")
        .build();

    return boardUpdateDtoRequest;
  }

  @Test
  public void 게시판_수정_성공(){
    //Given

    Board board = new Board("update","수정");

    BoardUpdateDto.Request request = createBoardUpdateRequest();
    Board newBoard = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);
    ReflectionTestUtils.setField(newBoard,"boardId", request.getBoardId());

    when(boardJpaRepository.findById(board.getBoardId())).thenReturn(Optional.ofNullable(board));
    when(boardJpaRepository.findById(fakeBoardId)).thenReturn(Optional.ofNullable(newBoard));

    //When
    boardUpdateService.update(request);

    //Then
    Board findBoard = boardJpaRepository.findById(fakeBoardId).get();
    assertThat(newBoard.getBoardId()).isEqualTo(findBoard.getBoardId());
    assertThat(newBoard.getNameEng()).isEqualTo(findBoard.getNameEng());
    assertThat(newBoard.getNameKor()).isEqualTo(findBoard.getNameKor());

  }

  @Test
  public void 게시판_수정_실패_존재하지_않는_게시판(){
    //Given
    BoardUpdateDto.Request request = createBoardUpdateRequest();

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardUpdateService.update(request));


    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.NO_SUCH_BOARD);

  }


  @Test
  public void 게시판_수정_실패_존재하는_한글_이름(){
    //Given
    BoardUpdateDto.Request request = createBoardUpdateRequest();
    Board board = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    when(boardJpaRepository.findById(board.getBoardId())).thenReturn(Optional.ofNullable(board));
    when(boardJpaRepository.findByNameKor(board.getNameKor())).thenReturn(Optional.ofNullable(board));

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardUpdateService.update(request));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.DUPLICATED_NAME_KOR);
  }

  @Test
  public void 게시판_수정_실패_존재하는_영어_이름(){
    //Given
    BoardUpdateDto.Request request = createBoardUpdateRequest();
    Board board = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    when(boardJpaRepository.findById(board.getBoardId())).thenReturn(Optional.ofNullable(board));
    when(boardJpaRepository.findByNameEng(board.getNameEng())).thenReturn(Optional.ofNullable(board));

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardUpdateService.update(request));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.DUPLICATED_NAME_ENG);
  }

  @Test
  public void 게시판_수정_실패_한글_이름_권한_중복(){
    //Given
    BoardUpdateDto.Request request = createBoardUpdateRequest();
    Board board = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    Authority authority = new Authority(request.getNameEng(), request.getNameKor());

    when(boardJpaRepository.findById(board.getBoardId())).thenReturn(Optional.ofNullable(board));
    when(authorityJpaRepository.findByNameKor(board.getNameKor())).thenReturn(Optional.ofNullable(authority));

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardUpdateService.update(request));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.CAN_NOT_USE_NAME_KOR);
  }

  @Test
  public void 게시판_수정_실패_영어_이름_권한_중복(){
    //Given
    BoardUpdateDto.Request request = createBoardUpdateRequest();
    Board board = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    Authority authority = new Authority(request.getNameEng(), request.getNameKor());

    when(boardJpaRepository.findById(board.getBoardId())).thenReturn(Optional.ofNullable(board));
    when(authorityJpaRepository.findByNameEng(board.getNameEng())).thenReturn(Optional.of(authority));

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardUpdateService.update(request));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.CAN_NOT_USE_NAME_ENG);
  }


  @Test
  public void 게시판_수정_실패_한글_이름_메뉴_중복(){
    //Given
    BoardUpdateDto.Request request = createBoardUpdateRequest();
    Board board = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    Menu menu = board.getMenu();

    when(boardJpaRepository.findById(board.getBoardId())).thenReturn(Optional.ofNullable(board));
    when(menuJpaRepository.findByNameKor(board.getNameKor())).thenReturn(Optional.ofNullable(menu));

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardUpdateService.update(request));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.CAN_NOT_USE_NAME_KOR);
  }

  @Test
  public void 게시판_수정_실패_영어_이름_메뉴_중복(){
    //Given
    BoardUpdateDto.Request request = createBoardUpdateRequest();
    Board board = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    Menu menu = board.getMenu();

    when(boardJpaRepository.findById(board.getBoardId())).thenReturn(Optional.ofNullable(board));
    when(menuJpaRepository.findByNameEng(board.getNameEng())).thenReturn(Optional.ofNullable(menu));

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardUpdateService.update(request));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.CAN_NOT_USE_NAME_ENG);
  }

  @Test
  public void 게시판_수정_실패_영어_이름_URL_중복(){
    //Given
    BoardUpdateDto.Request request = createBoardUpdateRequest();
    Board board = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    Menu menu = board.getMenu();

    when(boardJpaRepository.findById(board.getBoardId())).thenReturn(Optional.ofNullable(board));
    when(menuJpaRepository.findByUrl(board.getNameEng())).thenReturn(Optional.ofNullable(menu));

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardUpdateService.update(request));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.CAN_NOT_USE_NAME_ENG);
  }

}