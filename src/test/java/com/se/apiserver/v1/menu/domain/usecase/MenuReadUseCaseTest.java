package com.se.apiserver.v1.menu.domain.usecase;

import com.se.apiserver.v1.menu.domain.entity.MenuType;
import com.se.apiserver.v1.menu.infra.dto.MenuCreateDto;
import com.se.apiserver.v1.menu.infra.dto.MenuReadDto;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import org.aspectj.lang.annotation.Before;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MenuReadUseCaseTest {

    @Autowired
    MenuReadUseCase menuReadUseCase;

    @Autowired
    MenuCreateUseCase menuCreateUseCase;

    @Autowired
    MenuJpaRepository menuJpaRepository;

    //TODO 등록을 repo 이용으로 변경
    @BeforeEach
    public void init() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("2",
                "2", Arrays.asList(new SimpleGrantedAuthority("MENU_folder2_ACCESS"),new SimpleGrantedAuthority("MENU_freeboard2_ACCESS"))));


        MenuCreateDto.Response response = menuCreateUseCase.create(MenuCreateDto.Request.builder()
                .nameKor("폴더")
                .nameEng("folder")
                .description("폴더입니다")
                .url("folder")
                .menuOrder(1)
                .menuType(MenuType.FOLDER)
                .build());

        MenuCreateDto.Response response2 = menuCreateUseCase.create(MenuCreateDto.Request.builder()
                .nameKor("폴더2")
                .nameEng("folder2")
                .description("폴더2입니다")
                .url("folder2")
                .menuOrder(2)
                .menuType(MenuType.FOLDER)
                .build());


        menuCreateUseCase.create(MenuCreateDto.Request.builder()
                .nameKor("자유게시판")
                .nameEng("freeboard")
                .description("자유게시판입니다")
                .url("freeboard")
                .menuOrder(1)
                .menuType(MenuType.BOARD)
                .parentId(response.getMenuId())
                .build());

        menuCreateUseCase.create(MenuCreateDto.Request.builder()
                .nameKor("자유게시판2")
                .nameEng("freeboard2")
                .description("자유게시판2입니다")
                .url("freeboard2")
                .menuOrder(2)
                .menuType(MenuType.BOARD)
                .parentId(response2.getMenuId())
                .build());
    }

    @Test
    void 메뉴_조회_성공() {
        List<MenuReadDto.ReadAllResponse> responses = menuReadUseCase.readAll();
        Assertions.assertThat(responses.size()).isEqualTo(1);
    }
}