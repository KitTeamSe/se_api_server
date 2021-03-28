package com.se.apiserver.v1.menu.domain.usecase;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.usecase.authority.AuthorityCreateUseCase;
import com.se.apiserver.v1.authority.infra.dto.authority.AuthorityCreateDto;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.error.MenuErrorCode;
import com.se.apiserver.v1.menu.infra.dto.MenuCreateDto;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuCreateUseCase {

    private final MenuJpaRepository menuJpaRepository;

    private final AuthorityCreateUseCase authorityCreateUseCase;

    private final AuthorityJpaRepository authorityJpaRepository;

    @Transactional
    public MenuCreateDto.Response create(MenuCreateDto.Request request) {
        Menu.MenuBuilder menuBuilder = Menu.builder();

        menuBuilder.description(request.getDescription());
        menuBuilder.menuOrder(request.getMenuOrder());
        menuBuilder.menuType(request.getMenuType());

        if(menuJpaRepository.findByNameKor(request.getNameKor()).isPresent())
            throw new BusinessException(MenuErrorCode.DUPLICATED_MENU_NAME_KOR);
        menuBuilder.nameKor(request.getNameKor());

        if(menuJpaRepository.findByNameEng(request.getNameEng()).isPresent())
            throw new BusinessException(MenuErrorCode.DUPLICATED_MENU_NAME_ENG);
        menuBuilder.nameEng(request.getNameEng());

        if(menuJpaRepository.findByUrl(request.getUrl()).isPresent())
            throw new BusinessException(MenuErrorCode.DUPLICATED_URL);
        menuBuilder.url(request.getUrl());


        Menu menu = menuBuilder.build();

        if(request.getParentId() != null){
            Menu parent = menuJpaRepository.findById(request.getParentId()).orElseThrow(() -> new BusinessException(MenuErrorCode.NO_SUCH_MENU));
            menu.updateParent(parent);
        }


        Authority authority = authorityCreateUseCase.create(AuthorityCreateDto.Request.builder()
        .nameEng(menu.getNameEng())
        .nameKor(menu.getNameKor())
        .menu(menu).build());

        menu.updateAuthority(authority);
        menuJpaRepository.save(menu);

        return MenuCreateDto.Response.fromEntity(menu);
    }
}
