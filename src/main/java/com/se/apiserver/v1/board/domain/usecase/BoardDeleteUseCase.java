package com.se.apiserver.v1.board.domain.usecase;

import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.domain.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.dto.BoardDeleteDto;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardDeleteUseCase {

    private final BoardJpaRepository boardJpaRepository;

    public boolean delete(Long id){
        Board board = boardJpaRepository.findById(id).orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));
        boardJpaRepository.delete(board);
        return true;
    }
}
