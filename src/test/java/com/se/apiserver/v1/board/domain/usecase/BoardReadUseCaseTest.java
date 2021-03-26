package com.se.apiserver.v1.board.domain.usecase;

import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.dto.BoardReadDto;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardReadUseCaseTest {
    @Autowired
    BoardReadUseCase boardReadUseCase;
    @Autowired
    BoardJpaRepository boardJpaRepository;

    @Test
    void 게시판_읽기_성공() {
        //given
        Board board = Board.builder()
                .nameEng("freeboard")
                .nameKor("자유게시판")
                .build();
        boardJpaRepository.save(board);
        //when
        BoardReadDto.ReadResponse read = boardReadUseCase.read(board.getBoardId());
        //then
        Assertions.assertThat(read.getBoardId()).isEqualTo(board.getBoardId());
        Assertions.assertThat(read.getNameEng()).isEqualTo(board.getNameEng());
        Assertions.assertThat(read.getNameKor()).isEqualTo(board.getNameKor());
    }
}