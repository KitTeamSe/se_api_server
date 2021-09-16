package com.se.apiserver.v1.menu.application.service;

import com.se.apiserver.v1.board.application.service.BoardDeleteService;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.application.error.MenuErrorCode;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuDeleteService {
    private final MenuJpaRepository menuJpaRepository;
    private final BoardDeleteService boardDeleteService;
    public MenuDeleteService(
        MenuJpaRepository menuJpaRepository,
        BoardDeleteService boardDeleteService) {
        this.menuJpaRepository = menuJpaRepository;
        this.boardDeleteService = boardDeleteService;
    }

    @Transactional
    public void delete(Long id) {
        Menu menu = menuJpaRepository.findById(id)
            .orElseThrow(() -> new BusinessException(MenuErrorCode.NO_SUCH_MENU));
        validateIsRemovable(menu);
        boardDeleteService.delete(menu.getBoard().getBoardId());
        menuJpaRepository.delete(menu);
    }

    private void validateIsRemovable(Menu menu) {
        if (!menu.isRemovable())
            throw new BusinessException(MenuErrorCode.CHILD_REMOVE_FIRST);
    }
}
