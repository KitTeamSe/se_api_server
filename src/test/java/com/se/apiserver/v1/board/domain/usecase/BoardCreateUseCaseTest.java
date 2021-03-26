package com.se.apiserver.v1.board.domain.usecase;

import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.domain.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.dto.BoardCreateDto;
import com.se.apiserver.v1.board.infra.dto.BoardReadDto;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import com.se.apiserver.v1.menu.domain.error.MenuErrorCode;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import io.swagger.annotations.ApiModelProperty;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardCreateUseCaseTest {

    @Autowired
    BoardJpaRepository boardJpaRepository;

    @Autowired
    BoardCreateUseCase boardCreateUseCase;

    @Autowired
    MenuJpaRepository menuJpaRepository;

    @Test
    void 게시판_생성_성공() {
        //given
        //when
        BoardReadDto.ReadResponse readResponse = boardCreateUseCase.create(BoardCreateDto.Request.builder()
                .nameEng("freeboard")
                .nameKor("자유게시판")
                .menuOrder(1).build());
        //then
        Board board = boardJpaRepository.findById(readResponse.getBoardId()).get();
        Assertions.assertThat(board.getNameEng()).isEqualTo("freeboard");
        Assertions.assertThat(board.getNameKor()).isEqualTo("자유게시판");
        Assertions.assertThat(board.getMenu().getNameEng()).isEqualTo("freeboard");
        Assertions.assertThat(board.getMenu().getNameKor()).isEqualTo("자유게시판");
        Assertions.assertThat(board.getMenu().getMenuOrder()).isEqualTo(1);
        Assertions.assertThat(board.getMenu().getAuthority().getNameEng()).isEqualTo("MENU_freeboard_ACCESS");
        Assertions.assertThat(board.getMenu().getAuthority().getNameKor()).isEqualTo("자유게시판 접근");
    }

    @Test
    void 게시판_이미존재하는_영문명_실패() {
        Board board = Board.builder()
                .nameKor("테스트")
                .nameEng("freeboard")
                .build();
        boardJpaRepository.save(board);
        //given
        //when

        //then
        Assertions.assertThatThrownBy(() -> {
            BoardReadDto.ReadResponse readResponse = boardCreateUseCase.create(BoardCreateDto.Request.builder()
                    .nameEng("freeboard")
                    .nameKor("자유게시판")
                    .menuOrder(1).build());
        }).isInstanceOf(BusinessException.class).hasMessage(BoardErrorCode.DUPLICATED_NAME_ENG.getMessage());
    }

    @Test
    void 게시판_이미존재하는_한글명_실패() {
        //given
        Board board = Board.builder()
                .nameKor("자유게시판")
                .nameEng("test")
                .build();
        boardJpaRepository.save(board);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            BoardReadDto.ReadResponse readResponse = boardCreateUseCase.create(BoardCreateDto.Request.builder()
                    .nameEng("freeboard")
                    .nameKor("자유게시판")
                    .menuOrder(1).build());
        }).isInstanceOf(BusinessException.class).hasMessage(BoardErrorCode.DUPLICATED_NAME_KOR.getMessage());
    }

    @Test
    void 게시판_이미존재하는_몌뉴_영문명_실패() {
        Menu menu = Menu.builder()
                .nameEng("freeboard")
                .nameKor("테스트")
                .description("dasdasdsa")
                .menuOrder(1)
                .menuType(MenuType.BOARD)
                .url("dasdas").build();
        menuJpaRepository.save(menu);
        //given
        //when

        //then
        Assertions.assertThatThrownBy(() -> {
            BoardReadDto.ReadResponse readResponse = boardCreateUseCase.create(BoardCreateDto.Request.builder()
                    .nameEng("freeboard")
                    .nameKor("자유게시판")
                    .menuOrder(1).build());
        }).isInstanceOf(BusinessException.class).hasMessage(MenuErrorCode.DUPLICATED_MENU_NAME_ENG.getMessage());
    }

    @Test
    void 게시판_이미존재하는_몌뉴_한글명_실패() {
        Menu menu = Menu.builder()
                .nameEng("test")
                .nameKor("자유게시판")
                .description("dasdasdsa")
                .menuOrder(1)
                .menuType(MenuType.BOARD)
                .url("dasdas").build();
        menuJpaRepository.save(menu);
        //given
        //when

        //then
        Assertions.assertThatThrownBy(() -> {
            BoardReadDto.ReadResponse readResponse = boardCreateUseCase.create(BoardCreateDto.Request.builder()
                    .nameEng("freeboard")
                    .nameKor("자유게시판")
                    .menuOrder(1).build());
        }).isInstanceOf(BusinessException.class).hasMessage(MenuErrorCode.DUPLICATED_MENU_NAME_KOR.getMessage());
    }
}