package com.se.apiserver.v1.menu.domain.usecase;

import com.se.apiserver.v1.authority.domain.usecase.authority.AuthorityUpdateUseCase;
import com.se.apiserver.v1.authority.infra.dto.authority.AuthorityUpdateDto;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.error.MenuErrorCode;
import com.se.apiserver.v1.menu.infra.dto.MenuUpdateDto;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuUpdateUseCase {

    private final MenuJpaRepository menuJpaRepository;

    private final AuthorityUpdateUseCase authorityUpdateUseCase;

    @Transactional
    public MenuUpdateDto.Response update(MenuUpdateDto.Request request) {

        Menu menu = menuJpaRepository.findById(request.getMenuId()).orElseThrow(() -> new BusinessException(MenuErrorCode.NO_SUCH_MENU));
        if (request.getNameEng() != null && menuJpaRepository.findByNameEng(request.getNameEng()).isPresent())
            throw new BusinessException(MenuErrorCode.DUPLICATED_MENU_NAME_ENG);

        if(request.getNameEng() != null) {
            menu.updateNameEng(request.getNameEng());
            authorityUpdateUseCase.update(AuthorityUpdateDto.Request.builder()
                    .id(menu.getAuthority().getAuthorityId())
                    .nameEng(request.getNameEng()).build());
        }

        if (request.getNameKor() != null && menuJpaRepository.findByNameKor(request.getNameKor()).isPresent())
            throw new BusinessException(MenuErrorCode.DUPLICATED_MENU_NAME_KOR);

        if(request.getNameKor() != null) {
            menu.updateNameKor(request.getNameKor());
            authorityUpdateUseCase.update(AuthorityUpdateDto.Request.builder()
                    .id(menu.getAuthority().getAuthorityId())
                    .nameKor(request.getNameKor()).build());
        }

        if(request.getUrl() != null && menuJpaRepository.findByUrl(request.getUrl()).isPresent())
            throw new BusinessException(MenuErrorCode.DUPLICATED_URL);

        if(request.getUrl() != null)
            menu.updateUrl(request.getUrl());

        //TODO 게시판이랑 연관관계 생기면 수정해야함
        if(request.getMenuType() != null){
           menu.updateMenuType(request.getMenuType());
        }

        if(request.getParentId() != null){
            Menu parent = menuJpaRepository.findById(request.getParentId()).orElseThrow(() -> new BusinessException(MenuErrorCode.NO_SUCH_MENU));
            checkCycle(menu,parent);
            menu.updateParent(parent);
        }

        if(request.getDescription() != null)
            menu.updateDescription(request.getDescription());

        if(request.getMenuOrder() != null)
            menu.updateMenuOrder(request.getMenuOrder());

        menuJpaRepository.save(menu);

        return MenuUpdateDto.Response.fromEntity(menu);
    }

    private void checkCycle(Menu menu, Menu noChild) {
        if(dfs(menu, noChild))
            throw new BusinessException(MenuErrorCode.OCCUR_CYCLE);
    }

    private boolean dfs(Menu now, Menu noChild) {
        if(now == noChild)
            return true;
        for(Menu child : now.getChild()){
            return dfs(child, noChild);
        }
        return false;
    }
}
