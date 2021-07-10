package com.se.apiserver.v1.board.application.service;

import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.application.dto.BoardReadDto;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BoardReadServiceTest {
    @Autowired
    BoardReadService boardReadService;
    @Autowired
    BoardJpaRepository boardJpaRepository;

    @Test
    void 게시판_읽기_성공() {
        //given
        Board board = new Board("freeboard", "새로운게시판");
        boardJpaRepository.save(board);
        //when
        BoardReadDto.ReadResponse read = boardReadService.read(board.getBoardId());
        //then
        Assertions.assertThat(read.getBoardId()).isEqualTo(board.getBoardId());
        Assertions.assertThat(read.getNameEng()).isEqualTo(board.getNameEng());
        Assertions.assertThat(read.getNameKor()).isEqualTo(board.getNameKor());
    }
}