package com.se.apiserver.v1.board.application.service;

import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.application.dto.BoardCreateDto;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCreateService {

    private final BoardJpaRepository boardJpaRepository;
    private final MenuJpaRepository menuJpaRepository;
    private final AuthorityJpaRepository authorityJpaRepository;

    public Long create(BoardCreateDto.Request request){
        validateDuplicateNameKor(request.getNameKor());
        validateDuplicateNameEng(request.getNameEng());
        Board board = new Board(request.getNameEng(), request.getNameKor());
        boardJpaRepository.save(board);
        return board.getBoardId();
    }

    private void validateDuplicateNameEng(String nameEng) {
        if(boardJpaRepository.findByNameEng(nameEng).isPresent())
            throw new BusinessException(BoardErrorCode.DUPLICATED_NAME_ENG);
        if(authorityJpaRepository.findByNameEng(nameEng).isPresent())
            throw new BusinessException(BoardErrorCode.CAN_NOT_USE_NAME_ENG);
        if(menuJpaRepository.findByNameEng(nameEng).isPresent())
            throw new BusinessException(BoardErrorCode.CAN_NOT_USE_NAME_ENG);
        if(menuJpaRepository.findByUrl(nameEng).isPresent())
            throw new BusinessException(BoardErrorCode.CAN_NOT_USE_NAME_ENG);
    }

    private void validateDuplicateNameKor(String nameKor) {
        if(boardJpaRepository.findByNameKor(nameKor).isPresent())
            throw new BusinessException(BoardErrorCode.DUPLICATED_NAME_KOR);
        if(authorityJpaRepository.findByNameKor(nameKor).isPresent())
            throw new BusinessException(BoardErrorCode.CAN_NOT_USE_NAME_KOR);
        if(menuJpaRepository.findByNameKor(nameKor).isPresent())
            throw new BusinessException(BoardErrorCode.CAN_NOT_USE_NAME_KOR);
    }
}
