package com.se.apiserver.v1.menu.application.service;

import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import com.se.apiserver.v1.menu.application.error.MenuErrorCode;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuDeleteServiceTest {
    @Autowired
    MenuJpaRepository menuJpaRepository;
    @Autowired
    MenuDeleteService menuDeleteService;
    @Autowired
    AuthorityJpaRepository authorityJpaRepository;

    private Menu createData(String nameEng, String nameKor, String description, Integer order, MenuType menuType, String url){
        Menu menu = new Menu(nameEng, url, nameKor,order, description, menuType);
        menuJpaRepository.save(menu);
        return menu;
    }

    @Test
    void 메뉴_삭제_성공() {
        //given
        Menu menu = createData("freeboard1", "자유게시판1", "자유게시판입니다", 2, MenuType.BOARD, "freeboard1");

        Long menuId = menu.getMenuId();
        Long authorityId = menu.getAuthority().getAuthorityId();
        //when
        menuDeleteService.delete(menu.getMenuId());
        //then
        Assertions.assertThat(menuJpaRepository.findById(menuId).isEmpty()).isEqualTo(true);
        Assertions.assertThat(authorityJpaRepository.findById(authorityId).isEmpty()).isEqualTo(true);
    }

    @Test
    void 메뉴_삭제_자식있을때_실패() {
        //given
        Menu menu2 = createData("freeboard1", "자유게시판1", "자유게시판입니다", 2, MenuType.FOLDER, "freeboard1");
        Menu menu = createData("freeboard", "자유게시판", "자유게시판입니다", 1, MenuType.BOARD, "freeboard2");
        menu.updateParent(menu2);
        menuJpaRepository.save(menu);
        //when
        Assertions.assertThatThrownBy(() -> {
            menuDeleteService.delete(menu2.getMenuId());
        }).isInstanceOf(BusinessException.class).hasMessage(MenuErrorCode.CHILD_REMOVE_FIRST.getMessage());
    }
}