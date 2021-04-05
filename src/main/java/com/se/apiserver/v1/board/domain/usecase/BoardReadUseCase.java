package com.se.apiserver.v1.board.domain.usecase;

import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.domain.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.dto.BoardReadDto;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardReadUseCase {

    private final BoardJpaRepository boardJpaRepository;

    public BoardReadDto.ReadResponse read(Long id) {
        Board board = boardJpaRepository.findById(id).orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));
        return BoardReadDto.ReadResponse.fromEntity(board);
    }

    public List<BoardReadDto.ReadResponse> readAll() {
        List<Board> all = boardJpaRepository.findAll();
        List<BoardReadDto.ReadResponse> responses = all.stream()
                .map(b -> BoardReadDto.ReadResponse.fromEntity(b))
                .collect(Collectors.toList());
        return responses;
    }
}
