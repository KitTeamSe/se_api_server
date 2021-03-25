package com.se.apiserver.v1.menu.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.error.MenuErrorCode;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuDeleteUseCase {
    private final MenuJpaRepository menuJpaRepository;

    public boolean delete(Long id){
        Menu menu = menuJpaRepository.findById(id).orElseThrow(() -> new BusinessException(MenuErrorCode.NO_SUCH_MENU));

        if(menu.getChild().size() != 0)
            throw new BusinessException(MenuErrorCode.CHILD_REMOVE_FIRST);

        menuJpaRepository.delete(menu);
        return true;
    }
}
