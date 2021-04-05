package com.se.apiserver.v1.board.domain.usecase;

import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.domain.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.dto.BoardCreateDto;
import com.se.apiserver.v1.board.infra.dto.BoardReadDto;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardDeleteUseCaseTest {

    @Autowired
    BoardJpaRepository boardJpaRepository;

    @Autowired
    BoardCreateUseCase boardCreateUseCase;

    @Autowired
    MenuJpaRepository menuJpaRepository;

    @Autowired
    AuthorityJpaRepository authorityJpaRepository;

    @Autowired
    BoardDeleteUseCase boardDeleteUseCase;

    @Test
    void 게시판_삭제_성공() {
        //given
        Long id = boardCreateUseCase.create(BoardCreateDto.Request.builder()
                .nameEng("freeboard")
                .nameKor("자유게시판")
                .build());
        //when
        Board board = boardJpaRepository.findById(id).get();
        Long boardId = board.getBoardId();
        Long menuId = board.getMenu().getMenuId();
        Long authorityId = board.getMenu().getAuthority().getAuthorityId();
        boardDeleteUseCase.delete(id);
        //then
        Assertions.assertThat(boardJpaRepository.findById(boardId).isEmpty()).isEqualTo(true);
        Assertions.assertThat(authorityJpaRepository.findById(authorityId).isEmpty()).isEqualTo(true);
        Assertions.assertThat(menuJpaRepository.findById(menuId).isEmpty()).isEqualTo(true);
    }

    @Test
    void 게시판_미존재_실패() {
        //given
        Long id = boardCreateUseCase.create(BoardCreateDto.Request.builder()
                .nameEng("freeboard")
                .nameKor("자유게시판")
                .build());
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            boardDeleteUseCase.delete(100L);
        }).isInstanceOf(BusinessException.class).hasMessage(BoardErrorCode.NO_SUCH_BOARD.getMessage());
    }
}