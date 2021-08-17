package com.se.apiserver.v1.menu.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.application.error.MenuErrorCode;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuDeleteService {
    private final MenuJpaRepository menuJpaRepository;

    @Transactional
    public void delete(Long id) {
        Menu menu = menuJpaRepository.findById(id)
            .orElseThrow(() -> new BusinessException(MenuErrorCode.NO_SUCH_MENU));
        validateIsRemovable(menu);
        menuJpaRepository.delete(menu);
    }

    private void validateIsRemovable(Menu menu) {
        if (!menu.isRemovable())
            throw new BusinessException(MenuErrorCode.CHILD_REMOVE_FIRST);
    }
}
