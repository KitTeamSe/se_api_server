package com.se.apiserver.v1.board.domain.usecase;

import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.domain.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.dto.BoardCreateDto;
import com.se.apiserver.v1.board.infra.dto.BoardReadDto;
import com.se.apiserver.v1.board.infra.dto.BoardUpdateDto;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardUpdateUseCaseTest {
    @Autowired
    BoardCreateUseCase boardCreateUseCase;

    @Autowired
    BoardJpaRepository boardJpaRepository;

    @Autowired
    BoardUpdateUseCase boardUpdateUseCase;

    @Test
    void 게시판_수정_성공() {
        //given
        BoardReadDto.ReadResponse readResponse = boardCreateUseCase.create(BoardCreateDto.Request.builder()
                .menuOrder(1)
                .nameKor("자유게시판")
                .nameEng("freeboard")
                .build());
        //when
        boardUpdateUseCase.update(BoardUpdateDto.Request.builder()
        .boardId(readResponse.getBoardId())
        .nameEng("freeboard1")
        .nameKor("자유게시판1")
        .build());
        //then
        Board board = boardJpaRepository.findById(readResponse.getBoardId()).get();
        Assertions.assertThat(board.getNameKor()).isEqualTo("자유게시판1");
        Assertions.assertThat(board.getNameEng()).isEqualTo("freeboard1");
        Assertions.assertThat(board.getMenu().getNameKor()).isEqualTo("자유게시판1");
        Assertions.assertThat(board.getMenu().getNameEng()).isEqualTo("freeboard1");
        Assertions.assertThat(board.getMenu().getAuthority().getNameEng()).isEqualTo("MENU_freeboard1_ACCESS");
        Assertions.assertThat(board.getMenu().getAuthority().getNameKor()).isEqualTo("자유게시판1 접근");
    }

    @Test
    void 게시판_이미존재하는_게시판_영문명_실패() {
        //given
        BoardReadDto.ReadResponse readResponse = boardCreateUseCase.create(BoardCreateDto.Request.builder()
                .menuOrder(1)
                .nameKor("자유게시판")
                .nameEng("freeboard")
                .build());

        boardCreateUseCase.create(BoardCreateDto.Request.builder()
                .menuOrder(1)
                .nameKor("자유게시판1")
                .nameEng("freeboard1")
                .build());
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            boardUpdateUseCase.update(BoardUpdateDto.Request.builder()
                    .boardId(readResponse.getBoardId())
                    .nameEng("freeboard1")
                    .nameKor("테스트")
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(BoardErrorCode.DUPLICATED_NAME_ENG.getMessage());

    }
}