package com.se.apiserver.v1.menu.application.service;

import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.application.error.MenuErrorCode;
import com.se.apiserver.v1.menu.application.dto.MenuUpdateDto;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuUpdateService {

    private final MenuJpaRepository menuJpaRepository;
    private final AuthorityJpaRepository authorityJpaRepository;
    @Transactional
    public Long update(MenuUpdateDto.Request request) {
        Menu menu = menuJpaRepository.findById(request.getMenuId()).orElseThrow(() -> new BusinessException(MenuErrorCode.NO_SUCH_MENU));

        updateNameEngIfExist(menu, request.getNameEng());
        updateNameKorIfExist(menu, request.getNameKor());
        updateUrlIfExist(menu, request.getUrl());
        updateParentIfExist(menu, request.getParentId());
        updateDescriptionIfExist(menu, request.getDescription());
        updateMenuOrderIfExist(menu, request.getMenuOrder());
        menuJpaRepository.save(menu);
        return menu.getMenuId();
    }

    private void updateMenuOrderIfExist(Menu menu, Integer menuOrder) {
        if (menuOrder == null)
            return;
        menu.updateMenuOrder(menuOrder);
    }

    private void updateDescriptionIfExist(Menu menu, String description) {
        if (description == null)
            return;
        menu.updateDescription(description);
    }

    private void updateParentIfExist(Menu menu, Long parentId) {
        if (parentId == null)
            return;
        Menu parent = menuJpaRepository.findById(parentId).orElseThrow(() -> new BusinessException(MenuErrorCode.NO_SUCH_MENU));
        menu.updateParent(parent);
    }

    private void updateUrlIfExist(Menu menu, String url) {
        if (url == null)
            return;
        if (menu.getUrl().equals(url))
            return;
        validateDuplicateUrl(url);
        menu.updateUrl(url);
    }

    private void updateNameKorIfExist(Menu menu, String nameKor) {
        if (nameKor == null)
            return;
        if (menu.getNameKor().equals(nameKor))
            return;
        validateDuplicateNameKor(nameKor);
        menu.updateNameKor(nameKor);
    }

    private void updateNameEngIfExist(Menu menu, String nameEng) {
        if (nameEng == null)
            return;
        if (menu.getNameEng().equals(nameEng))
            return;
        validateDuplicateNameEng(nameEng);
        menu.updateNameEng(nameEng);
    }

    private void validateDuplicateUrl(String url) {
        if (menuJpaRepository.findByUrl(url).isPresent())
            throw new BusinessException(MenuErrorCode.DUPLICATED_URL);
    }

    private void validateDuplicateNameEng(String nameEng) {
        if (menuJpaRepository.findByNameEng(nameEng).isPresent())
            throw new BusinessException(MenuErrorCode.DUPLICATED_MENU_NAME_ENG);
        if (authorityJpaRepository.findByNameEng(nameEng).isPresent())
            throw new BusinessException(MenuErrorCode.CAN_NOT_USE_NAME_ENG);
    }

    private void validateDuplicateNameKor(String nameKor) {
        if (menuJpaRepository.findByNameKor(nameKor).isPresent())
            throw new BusinessException(MenuErrorCode.DUPLICATED_MENU_NAME_KOR);
        if (authorityJpaRepository.findByNameKor(nameKor).isPresent())
            throw new BusinessException(MenuErrorCode.CAN_NOT_USE_NAME_KOR);
    }


}
