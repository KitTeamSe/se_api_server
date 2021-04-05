package com.se.apiserver.v1.menu.application.service;

import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.application.error.MenuErrorCode;
import com.se.apiserver.v1.menu.application.dto.MenuCreateDto;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuCreateService {

    private final MenuJpaRepository menuJpaRepository;
    private final AuthorityJpaRepository authorityJpaRepository;

    @Transactional
    public Long create(MenuCreateDto.Request request) {
        validateDuplicateNameKor(request.getNameKor());
        validateDuplicateNameEng(request.getNameEng());
        validateDuplicateUrl(request.getUrl());

        boolean hasParent = (request.getParentId() != null);
        Menu menu = generateMenu(request, hasParent);

        menuJpaRepository.save(menu);
        return menu.getMenuId();
    }

    private Menu generateMenu(MenuCreateDto.Request request, boolean hasParent) {
        if(hasParent){
            Menu parent = menuJpaRepository.findById(request.getParentId())
                    .orElseThrow(() -> new BusinessException(MenuErrorCode.NO_SUCH_MENU));
            return new Menu(request.getNameEng(), request.getUrl(), request.getNameKor(),
            request.getMenuOrder(), request.getDescription(), request.getMenuType(), parent);
        }

        return new Menu(request.getNameEng(), request.getUrl(), request.getNameKor(),
                request.getMenuOrder(), request.getDescription(), request.getMenuType());
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
