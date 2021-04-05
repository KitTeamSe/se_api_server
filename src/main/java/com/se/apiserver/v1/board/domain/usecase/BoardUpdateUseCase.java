package com.se.apiserver.v1.board.domain.usecase;

import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.domain.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.dto.BoardReadDto;
import com.se.apiserver.v1.board.infra.dto.BoardUpdateDto;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.menu.domain.usecase.MenuUpdateUseCase;
import com.se.apiserver.v1.menu.infra.dto.MenuUpdateDto;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardUpdateUseCase {

    private final BoardJpaRepository boardJpaRepository;
    private final MenuJpaRepository menuJpaRepository;
    private final AuthorityJpaRepository authorityJpaRepository;

    @Transactional
    public Long update(BoardUpdateDto.Request request){
        Board board = boardJpaRepository.findById(request.getBoardId()).orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));
        validateDuplicateNameKor(request.getNameKor());
        validateDuplicateNameEng(request.getNameEng());
        updateNameEngIfExist(board, request.getNameEng());
        updateNameKorIfExist(board, request.getNameKor());
        boardJpaRepository.save(board);
        return board.getBoardId();
    }

    private void updateNameKorIfExist(Board board, String nameKor) {
        if(nameKor != null)
            board.updateNameKor(nameKor);
    }

    private void updateNameEngIfExist(Board board, String nameEng) {
        if(nameEng != null)
            board.updateNameEng(nameEng);
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
