package com.se.apiserver.v1.menu.application.service;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import com.se.apiserver.v1.menu.application.error.MenuErrorCode;
import com.se.apiserver.v1.menu.application.dto.MenuUpdateDto;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@SpringBootTest
@Transactional
class MenuUpdateServiceTest {
    @Autowired
    MenuJpaRepository menuJpaRepository;
    @Autowired
    MenuUpdateService menuUpdateService;
    @Autowired
    AuthorityJpaRepository authorityJpaRepository;


    @BeforeEach
    void setAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("2",
                "2", Arrays.asList(new SimpleGrantedAuthority("MENU_MANAGE"))));
    }



    private Menu createData(String nameEng, String nameKor, String description, Integer order, MenuType menuType, String url){
        Menu menu = new Menu(nameEng, url, nameKor,order, description, menuType);
        menuJpaRepository.save(menu);
        return menu;
    }

    @Test
    void 메뉴_수정_성공() {
        //given
        Menu menu2 = createData("freeboard1", "자유게시판1", "자유게시판입니다", 2, MenuType.FOLDER, "freeboard1");
        menuJpaRepository.save(menu2);

        Menu menu = createData("freeboard", "자유게시판", "자유게시판입니다", 1, MenuType.BOARD, "freeboard");
        menuJpaRepository.save(menu);

        //when
        menuUpdateService.update(MenuUpdateDto.Request.builder()
        .description("가나다라")
        .menuOrder(3)
        .nameKor("교수게시판")
        .nameEng("teacher")
        .menuId(menu.getMenuId())
        .parentId(menu2.getMenuId())
        .build()
        );
        //then
        Menu updated = menuJpaRepository.findById(menu.getMenuId()).get();
        Assertions.assertThat(updated.getDescription()).isEqualTo("가나다라");
        Assertions.assertThat(updated.getNameKor()).isEqualTo("교수게시판");
        Assertions.assertThat(updated.getNameEng()).isEqualTo("teacher");
        Assertions.assertThat(updated.getMenuOrder()).isEqualTo(3);

        Authority authority2 = updated.getAuthority();
        Assertions.assertThat(authority2.getNameKor()).isEqualTo("교수게시판");
        Assertions.assertThat(authority2.getNameEng()).isEqualTo("teacher");
    }

    @Test
    void 메뉴_수정_사이클발생_실패() {
        //given
        Menu menu1 = createData("freeboard", "자유게시판", "자유게시판입니다", 1, MenuType.FOLDER, "freeboard");
        Menu menu2 = createData("freeboard1", "자유", "자유게시판입니다", 2, MenuType.FOLDER, "freeboard1");
        menu2.updateParent(menu1);
        menuJpaRepository.save(menu2);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            menuUpdateService.update(MenuUpdateDto.Request.builder()
                    .description("가나다라")
                    .menuOrder(3)
                    .nameKor("교수게시판")
                    .nameEng("teacher")
                    .menuId(menu1.getMenuId())
                    .parentId(menu2.getMenuId())
                    .build()
            );
        }).isInstanceOf(BusinessException.class).hasMessage(MenuErrorCode.OCCUR_CYCLE.getMessage());
    }

    @Test
    void 메뉴_수정_한국명중복_실패() {
        //given
        Menu menu1 = createData("freeboard", "자유게시판", "자유게시판입니다", 1, MenuType.FOLDER, "freeboard");
        menuJpaRepository.save(menu1);
        Menu menu2 = createData("freeboard1", "자유게시판1", "자유게시판입니다", 2, MenuType.BOARD, "freeboard1");
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            menuUpdateService.update(MenuUpdateDto.Request.builder()
                    .description("가나다라")
                    .menuOrder(3)
                    .nameKor("자유게시판")
                    .nameEng("teacher")
                    .menuId(menu1.getMenuId())
                    .parentId(menu2.getMenuId())
                    .build()
            );
        }).isInstanceOf(BusinessException.class).hasMessage(MenuErrorCode.DUPLICATED_MENU_NAME_KOR.getMessage());
    }

    @Test
    void 메뉴_수정_영문명중복_실패() {
        //given
        Menu menu1 = createData("freeboard", "자유게시판", "자유게시판입니다", 1, MenuType.FOLDER, "freeboard");
        Menu menu2 = createData("freeboard1", "자유게시판1", "자유게시판입니다", 2, MenuType.BOARD, "freeboard1");
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            menuUpdateService.update(MenuUpdateDto.Request.builder()
                    .description("가나다라")
                    .menuOrder(3)
                    .nameKor("자유게시판32")
                    .nameEng("freeboard1")
                    .menuId(menu1.getMenuId())
                    .parentId(menu2.getMenuId())
                    .build()
            );
        }).isInstanceOf(BusinessException.class).hasMessage(MenuErrorCode.DUPLICATED_MENU_NAME_ENG.getMessage());
    }


    @Test
    void 메뉴_수정_URL중복_실패() {
        //given
        Menu menu1 = createData("freeboard2", "자유게시판", "자유게시판입니다", 1, MenuType.FOLDER, "freeboard1");
        Menu menu2 = createData("freeboard1", "자유게시판1", "자유게시판입니다", 2, MenuType.BOARD, "freeboard2");
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            menuUpdateService.update(MenuUpdateDto.Request.builder()
                    .menuId(menu1.getMenuId())
                    .url("freeboard2")
                    .build()
            );
        }).isInstanceOf(BusinessException.class).hasMessage(MenuErrorCode.DUPLICATED_URL.getMessage());
    }


}