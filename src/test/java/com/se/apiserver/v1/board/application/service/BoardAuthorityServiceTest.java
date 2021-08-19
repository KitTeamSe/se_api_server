package com.se.apiserver.v1.board.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BoardAuthorityServiceTest {
  @Mock
  private BoardJpaRepository boardJpaRepository;

  @Mock
  private AccountContextService accountContextService;

  @InjectMocks
  private BoardAuthorityService boardAuthorityService;

  @Test
  public void 게시판_접근_권한_확인_성공(){
    //Given
    Board board = new Board("freeboard","자유게시판");

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    Set<String> authorityList = new HashSet<>();
    authorityList.add("MENU_MANAGE");

    when(boardJpaRepository.findById(fakeBoardId)).thenReturn(Optional.ofNullable(board));

    when(accountContextService.getContextAuthorities()).thenReturn(authorityList);

    //When
    //Then
    assertDoesNotThrow(() -> boardAuthorityService.validateAccessAuthority(fakeBoardId));
  }

  @Test
  public void 게시판_관리_권한_확인_성공(){
    //Given
    Board board = new Board("freeboard","자유게시판");

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    Set<String> authorityList = new HashSet<>();
    authorityList.add("MENU_MANAGE");

    when(boardJpaRepository.findById(fakeBoardId)).thenReturn(Optional.ofNullable(board));

    when(accountContextService.getContextAuthorities()).thenReturn(authorityList);

    //When
    //Then
    assertDoesNotThrow(() -> boardAuthorityService.validateManageAuthority(fakeBoardId));
  }

  @Test
  public void 게시판_접근_권한_확인_실패_권한_없음(){
    //Given
    Board board = new Board("freeboard","자유게시판");

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    when(boardJpaRepository.findById(fakeBoardId)).thenReturn(Optional.ofNullable(board));

    //When
    AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> boardAuthorityService.validateAccessAuthority(fakeBoardId));

    //Then
    assertThat(exception.getMessage()).isEqualTo(new AccessDeniedException("접근 권한이 없습니다").getMessage());
  }

  @Test
  public void 게시판_관리_권한_확인_실패_권한_없음(){
    //Given
    Board board = new Board("freeboard","자유게시판");

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    when(boardJpaRepository.findById(fakeBoardId)).thenReturn(Optional.ofNullable(board));

    //When
    AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> boardAuthorityService.validateManageAuthority(fakeBoardId));

    //Then
    assertThat(exception.getMessage()).isEqualTo(new AccessDeniedException("관리 권한이 없습니다").getMessage());
  }

  @Test
  public void 게시판_접근_권한_확인_실패_게시판_없음(){
    //Given
    Board board = new Board("freeboard","자유게시판");

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardAuthorityService.validateAccessAuthority(fakeBoardId));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.NO_SUCH_BOARD);
  }

  @Test
  public void 게시판_관리_권한_확인_실패_게시판_없음(){
    //Given
    Board board = new Board("freeboard","자유게시판");

    Long fakeBoardId = 1l;
    ReflectionTestUtils.setField(board,"boardId", fakeBoardId);

    //When
    BusinessException exception = assertThrows(BusinessException.class, () -> boardAuthorityService.validateManageAuthority(fakeBoardId));

    //Then
    assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.NO_SUCH_BOARD);
  }
}