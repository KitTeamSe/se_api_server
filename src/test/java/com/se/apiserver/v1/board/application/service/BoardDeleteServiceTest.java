package com.se.apiserver.v1.board.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.board.application.dto.BoardDeleteDto;
import com.se.apiserver.v1.board.application.dto.BoardDeleteDto.Request;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BoardDeleteServiceTest {

  @Mock
  public BoardJpaRepository boardJpaRepository;

  @InjectMocks
  private BoardDeleteService boardDeleteService;


  private Request createBoardDeleteRequest(){
    Request boardDeleteRequest = Request.builder().boardId(1L).build();

    return boardDeleteRequest;
  }

  @Test
  public void 게시판_삭제_성공(){
    //Given
    BoardDeleteDto.Request request = createBoardDeleteRequest();
    Board board = new Board("freeboard","자유게시판");

    Long fakeBoardId = 1L;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    when(boardJpaRepository.findById(fakeBoardId)).thenReturn(Optional.ofNullable(board));

    //When
    boolean result =  boardDeleteService.delete(request.getBoardId());

    //Then
    assertThat(result).isEqualTo(true);
  }


  @Test
  public void 게시판_삭제_실패_존재하지_않는_게시판(){
    //Given
    BoardDeleteDto.Request request = createBoardDeleteRequest();

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardDeleteService.delete(request.getBoardId()));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.NO_SUCH_BOARD);
  }

}