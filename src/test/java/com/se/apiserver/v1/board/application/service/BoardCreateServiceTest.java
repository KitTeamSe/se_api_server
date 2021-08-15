package com.se.apiserver.v1.board.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.any;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.board.application.dto.BoardCreateDto;
import com.se.apiserver.v1.board.application.dto.BoardCreateDto.Request;
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
class BoardCreateServiceTest {

  @Mock
  public BoardJpaRepository boardJpaRepository;

  @Mock
  private MenuJpaRepository menuJpaRepository;

  @Mock
  private AuthorityJpaRepository authorityJpaRepository;

  @InjectMocks
  private BoardCreateService boardCreateService;

  private Request createBoardCreateRequest(){
    Request boardCreateRequest = Request.builder().nameEng("freeboard").nameKor("자유게시판").build();

    return boardCreateRequest;
  }

  @Test
  public void 게시판_생성_성공(){
    //Given
    BoardCreateDto.Request request = createBoardCreateRequest();
    Board board = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    when(boardJpaRepository.save(any())).thenReturn(board);
    when(boardJpaRepository.findById(fakeBoardId)).thenReturn(Optional.ofNullable(board));

    //When
    Long newId = boardCreateService.create(request);

    //Then
    Board findBoard = boardJpaRepository.findById(fakeBoardId).get();
    assertThat(board.getBoardId()).isEqualTo(findBoard.getBoardId());
    assertThat(board.getNameEng()).isEqualTo(findBoard.getNameEng());
    assertThat(board.getNameKor()).isEqualTo(findBoard.getNameKor());
  }

  @Test
  public void 게시판_생성_실패_존재하는_한글_이름(){
    //Given
    BoardCreateDto.Request request = createBoardCreateRequest();
    Board board = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    when(boardJpaRepository.findByNameKor(board.getNameKor())).thenReturn(Optional.ofNullable(board));

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardCreateService.create(request));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.DUPLICATED_NAME_KOR);
  }

  @Test
  public void 게시판_생성_실패_존재하는_영어_이름(){
    //Given
    BoardCreateDto.Request request = createBoardCreateRequest();
    Board board = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    when(boardJpaRepository.findByNameEng(board.getNameEng())).thenReturn(Optional.ofNullable(board));

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardCreateService.create(request));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.DUPLICATED_NAME_ENG);
  }

  @Test
  public void 게시판_생성_실패_한글_이름_권한_중복(){
    //Given
    BoardCreateDto.Request request = createBoardCreateRequest();
    Board board = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    Authority authority = new Authority(request.getNameEng(), request.getNameKor());

    when(authorityJpaRepository.findByNameKor(board.getNameKor())).thenReturn(Optional.ofNullable(authority));

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardCreateService.create(request));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.CAN_NOT_USE_NAME_KOR);
  }

  @Test
  public void 게시판_생성_실패_영어_이름_권한_중복(){
    //Given
    BoardCreateDto.Request request = createBoardCreateRequest();
    Board board = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    Authority authority = new Authority(request.getNameEng(), request.getNameKor());

    when(authorityJpaRepository.findByNameEng(board.getNameEng())).thenReturn(Optional.of(authority));

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardCreateService.create(request));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.CAN_NOT_USE_NAME_ENG);
  }


  @Test
  public void 게시판_생성_실패_한글_이름_메뉴_중복(){
    //Given
    BoardCreateDto.Request request = createBoardCreateRequest();
    Board board = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    Menu menu = board.getMenu();

    when(menuJpaRepository.findByNameKor(board.getNameKor())).thenReturn(Optional.ofNullable(menu));

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardCreateService.create(request));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.CAN_NOT_USE_NAME_KOR);
  }

  @Test
  public void 게시판_생성_실패_영어_이름_메뉴_중복(){
    //Given
    BoardCreateDto.Request request = createBoardCreateRequest();
    Board board = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    Menu menu = board.getMenu();

    when(menuJpaRepository.findByNameEng(board.getNameEng())).thenReturn(Optional.ofNullable(menu));

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardCreateService.create(request));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.CAN_NOT_USE_NAME_ENG);
  }

  @Test
  public void 게시판_생성_실패_영어_이름_URL_중복(){
    //Given
    BoardCreateDto.Request request = createBoardCreateRequest();
    Board board = new Board(request.getNameEng(),request.getNameKor());

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    Menu menu = board.getMenu();

    when(menuJpaRepository.findByUrl(board.getNameEng())).thenReturn(Optional.ofNullable(menu));

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardCreateService.create(request));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.CAN_NOT_USE_NAME_ENG);
  }

}