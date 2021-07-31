package com.se.apiserver.v1.board.application.service;

import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardDeleteService {

    private final BoardJpaRepository boardJpaRepository;

    @Transactional
    public boolean delete(String nameEng){
        Board board = boardJpaRepository.findByNameEng(nameEng).orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));
        boardJpaRepository.delete(board);
        return true;
    }
}
