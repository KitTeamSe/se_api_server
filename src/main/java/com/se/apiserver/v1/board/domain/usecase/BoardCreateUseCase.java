package com.se.apiserver.v1.board.domain.usecase;

import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.domain.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.dto.BoardCreateDto;
import com.se.apiserver.v1.board.infra.dto.BoardReadDto;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import com.se.apiserver.v1.menu.domain.error.MenuErrorCode;
import com.se.apiserver.v1.menu.domain.usecase.MenuCreateUseCase;
import com.se.apiserver.v1.menu.infra.dto.MenuCreateDto;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCreateUseCase {

    private final BoardJpaRepository boardJpaRepository;

    private final MenuJpaRepository menuJpaRepository;

    private final MenuCreateUseCase menuCreateUseCase;

    public BoardReadDto.ReadResponse create(BoardCreateDto.Request request){
        if(boardJpaRepository.findByNameEng(request.getNameEng()).isPresent())
            throw new BusinessException(BoardErrorCode.DUPLICATED_NAME_ENG);

        if(boardJpaRepository.findByNameKor(request.getNameKor()).isPresent())
            throw new BusinessException(BoardErrorCode.DUPLICATED_NAME_KOR);

        MenuCreateDto.Response response = menuCreateUseCase.create(MenuCreateDto.Request.builder()
                .menuType(MenuType.BOARD)
                .nameEng(request.getNameEng())
                .nameKor(request.getNameKor())
                .menuOrder(request.getMenuOrder())
                .description(request.getNameKor())
                .url(request.getNameEng())
                .build());

        Menu menu = menuJpaRepository.findById(response.getMenuId()).orElseThrow(() -> new BusinessException(MenuErrorCode.NO_SUCH_MENU));

        Board board = Board.builder()
                .nameEng(request.getNameEng())
                .nameKor(request.getNameKor())
                .menu(menu).build();

        boardJpaRepository.save(board);


        return BoardReadDto.ReadResponse.fromEntity(board);
    }
}
