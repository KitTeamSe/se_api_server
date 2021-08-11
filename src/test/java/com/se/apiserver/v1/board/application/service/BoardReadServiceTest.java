package com.se.apiserver.v1.board.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.board.application.dto.BoardCreateDto;
import com.se.apiserver.v1.board.application.dto.BoardReadDto;
import com.se.apiserver.v1.board.application.dto.BoardReadDto.ReadResponse;
import com.se.apiserver.v1.board.application.dto.BoardUpdateDto;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


@ExtendWith(MockitoExtension.class)
class BoardReadServiceTest {

  @Mock
  public BoardJpaRepository boardJpaRepository;

  @Mock
  private MenuJpaRepository menuJpaRepository;

  @Mock
  private AuthorityJpaRepository authorityJpaRepository;

  @InjectMocks
  private BoardReadService boardReadService;

  private BoardReadDto.Request createBoardReadRequest(){
    BoardReadDto.Request boardReadRequest = BoardReadDto.Request.builder()
        .nameEng("freeboard")
        .build();

    return boardReadRequest;
  }

  @Test
  public void 게시판_조회_성공(){
    //Given
    BoardReadDto.Request request = createBoardReadRequest();
    Board board = new Board(request.getNameEng(),"자유게시판");

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    when(boardJpaRepository.findByNameEng(board.getNameEng())).thenReturn(Optional.ofNullable(board));

    //When
    ReadResponse readResponse = boardReadService.readByNameEng(request.getNameEng());

    //Then
    assertThat(readResponse).isEqualTo(ReadResponse.fromEntity(board));

  }

  @Test
  public void 게시판_조회_성공_전부_조회(){
    //Given
    Board board1 = new Board("freeboard1","자유게시판");
    Board board2 = new Board("freeboard2","자유게시판");

    Long fakeBoardId1 = 1l;
    ReflectionTestUtils.setField(board1,"boardId", fakeBoardId1);

    Long fakeBoardId2 = 2l;
    ReflectionTestUtils.setField(board2,"boardId", fakeBoardId2);

    List<Board> boardAll = new ArrayList<>();
    boardAll.add(board1);
    boardAll.add(board2);

    when(boardJpaRepository.findAll()).thenReturn(boardAll);

    //When
    List<ReadResponse> readResponses = boardReadService.readAll();

    //Then
    List<ReadResponse> expectedReadResponses = new ArrayList<>();
    expectedReadResponses.add(ReadResponse.fromEntity(board1));
    expectedReadResponses.add(ReadResponse.fromEntity(board2));
    assertThat(readResponses).isEqualTo(expectedReadResponses);

  }

  @Test
  public void 게시판_조회_실패_존재하지_않는_게시판(){

    //Given
    BoardReadDto.Request request = createBoardReadRequest();

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardReadService.readByNameEng(request.getNameEng()));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.NO_SUCH_BOARD);

  }

}