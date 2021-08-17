package com.se.apiserver.v1.menu.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.application.error.MenuErrorCode;
import com.se.apiserver.v1.menu.application.dto.MenuReadDto;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuReadService {

    private final MenuJpaRepository menuJpaRepository;
    private final AccountContextService accountContextService;

    public MenuReadService(
        MenuJpaRepository menuJpaRepository,
        AccountContextService accountContextService) {
        this.menuJpaRepository = menuJpaRepository;
        this.accountContextService = accountContextService;
    }

    public MenuReadDto.ReadResponse read(Long id) {
        Menu menu = menuJpaRepository.findById(id)
            .orElseThrow(() -> new BusinessException(MenuErrorCode.NO_SUCH_MENU));
        return MenuReadDto.ReadResponse.fromEntity(menu);
    }

    public List<MenuReadDto.ReadAllResponse> readAll() {
        Set<String> authorities = accountContextService.getContextAuthorities();
        List<Menu> menus = menuJpaRepository.findAllRootMenu();
        List<MenuReadDto.ReadAllResponse> res = menus.stream()
                .filter(menu -> menu.canAccess(authorities) == true)
                .map(menu -> MenuReadDto.ReadAllResponse.fromEntity(menu, authorities))
                .collect(Collectors.toList());
        return res;
    }
}
