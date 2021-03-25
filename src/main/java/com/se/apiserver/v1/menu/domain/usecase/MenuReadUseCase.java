package com.se.apiserver.v1.menu.domain.usecase;

import com.se.apiserver.security.service.AccountDetailService;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.error.MenuErrorCode;
import com.se.apiserver.v1.menu.infra.dto.MenuReadDto;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuReadUseCase {

    private final MenuJpaRepository menuJpaRepository;

    public MenuReadDto.ReadResponse read(Long id) {
        Menu menu = menuJpaRepository.findById(id).orElseThrow(() -> new BusinessException(MenuErrorCode.NO_SUCH_MENU));
        return MenuReadDto.ReadResponse.fromEntity(menu);
    }

    public List<MenuReadDto.ReadAllResponse> readAll() {
        Set<String> authoritySet = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        System.out.println(authoritySet.stream().findFirst().get());
        List<Menu> menus = menuJpaRepository.findIncludeAuthorities(authoritySet);
        System.out.println(menus.size());
        List<MenuReadDto.ReadAllResponse> res = new ArrayList<>();

        for(Menu menu : menus){
            if(authoritySet.contains(menu.getAuthority().getAuthority()))
                res.add(MenuReadDto.ReadAllResponse.fromEntity(menu, authoritySet));
        }

        return res;
    }
}
