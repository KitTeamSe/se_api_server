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
        Board board = new Board("freeboard", "자유게시판");
        boardJpaRepository.save(board);
        //when
        boardUpdateUseCase.update(BoardUpdateDto.Request.builder()
        .boardId(board.getBoardId())
        .nameEng("freeboard1")
        .nameKor("자유게시판1")
        .build());
        //then
        Board updated = boardJpaRepository.findById(board.getBoardId()).get();
        Assertions.assertThat(updated.getNameKor()).isEqualTo("자유게시판1");
        Assertions.assertThat(updated.getNameEng()).isEqualTo("freeboard1");
        Assertions.assertThat(updated.getMenu().getNameKor()).isEqualTo("자유게시판1");
        Assertions.assertThat(updated.getMenu().getNameEng()).isEqualTo("freeboard1");
        Assertions.assertThat(updated.getMenu().getAuthority().getNameEng()).isEqualTo("freeboard1");
        Assertions.assertThat(updated.getMenu().getAuthority().getNameKor()).isEqualTo("자유게시판1");
    }

    @Test
    void 게시판_이미존재하는_게시판_영문명_실패() {
        //given
        Long id = boardCreateUseCase.create(BoardCreateDto.Request.builder()
                .nameKor("자유게시판")
                .nameEng("freeboard")
                .build());

        boardCreateUseCase.create(BoardCreateDto.Request.builder()
                .nameKor("자유게시판1")
                .nameEng("freeboard1")
                .build());
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            boardUpdateUseCase.update(BoardUpdateDto.Request.builder()
                    .boardId(id)
                    .nameEng("freeboard1")
                    .nameKor("테스트")
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(BoardErrorCode.DUPLICATED_NAME_ENG.getMessage());

    }
}