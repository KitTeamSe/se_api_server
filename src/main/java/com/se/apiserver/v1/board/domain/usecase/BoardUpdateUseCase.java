package com.se.apiserver.v1.board.domain.usecase;

import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.domain.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.dto.BoardReadDto;
import com.se.apiserver.v1.board.infra.dto.BoardUpdateDto;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.menu.domain.usecase.MenuUpdateUseCase;
import com.se.apiserver.v1.menu.infra.dto.MenuUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardUpdateUseCase {

    private final BoardJpaRepository boardJpaRepository;

    private final MenuUpdateUseCase menuUpdateUseCase;

    public BoardReadDto.ReadResponse update(BoardUpdateDto.Request request){
        Board board = boardJpaRepository.findById(request.getBoardId()).orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));

        if(request.getNameEng() != null && boardJpaRepository.findByNameEng(request.getNameEng()).isPresent())
            throw new BusinessException(BoardErrorCode.DUPLICATED_NAME_ENG);

        if(request.getNameEng() != null)
            board.updateNameEng(request.getNameEng());

        if(request.getNameKor() != null && boardJpaRepository.findByNameKor(request.getNameKor()).isPresent())
            throw new BusinessException(BoardErrorCode.DUPLICATED_NAME_KOR);

        if(request.getNameKor() != null)
            board.updateNameKor(request.getNameKor());


        menuUpdateUseCase.update(MenuUpdateDto.Request.builder()
        .menuId(board.getMenu().getMenuId())
        .nameEng(request.getNameEng())
        .nameKor(request.getNameKor())
        .build());

        boardJpaRepository.save(board);

        return BoardReadDto.ReadResponse.fromEntity(board);
    }


}
