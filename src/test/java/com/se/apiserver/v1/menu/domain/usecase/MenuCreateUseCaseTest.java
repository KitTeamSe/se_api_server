package com.se.apiserver.v1.menu.domain.usecase;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.error.AuthorityErrorCode;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import com.se.apiserver.v1.menu.domain.error.MenuErrorCode;
import com.se.apiserver.v1.menu.infra.dto.MenuCreateDto;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MenuCreateUseCaseTest {

    @Autowired
    MenuJpaRepository menuJpaRepository;

    @Autowired
    MenuCreateUseCase menuCreateUseCase;

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
    void 메뉴_등록_성공() {
        //given
        //when
        menuCreateUseCase.create(MenuCreateDto.Request.builder()
                .nameEng("freeboard")
                .nameKor("자유게시판")
                .menuOrder(1)
                .description("자유게시판입니다.")
                .url("freeboard")
                .menuType(MenuType.BOARD)
                .build());
        //then
        Menu menu = menuJpaRepository.findByNameEng("freeboard").get();
        Assertions.assertThat(menu.getDescription()).isEqualTo("자유게시판입니다.");
        Assertions.assertThat(menu.getNameKor()).isEqualTo("자유게시판");
        Assertions.assertThat(menu.getNameEng()).isEqualTo("freeboard");
        Assertions.assertThat(menu.getMenuOrder()).isEqualTo(1);
        Assertions.assertThat(menu.getAuthority().getNameEng()).isEqualTo("freeboard");
        Assertions.assertThat(menu.getAuthority().getNameKor()).isEqualTo("자유게시판");
    }

    @Test
    void 메뉴_등록_영문명_중복_실패() {
        //given
        Menu menu = createData("freeboard", "자유게시판", "자유게시판입니다", 1, MenuType.BOARD, "freeboard");
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            menuCreateUseCase.create(MenuCreateDto.Request.builder()
                    .nameEng("freeboard")
                    .nameKor("게시판")
                    .menuOrder(1)
                    .description("자유게시판입니다.")
                    .url("freeboard22")
                    .menuType(MenuType.BOARD)
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(MenuErrorCode.DUPLICATED_MENU_NAME_ENG.getMessage());
    }

    @Test
    void 메뉴_등록_한국명_중복_실패() {
        //given
        Menu menu = createData("freeboard", "자유게시판", "자유게시판입니다", 1, MenuType.BOARD, "freeboard");
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            menuCreateUseCase.create(MenuCreateDto.Request.builder()
                    .nameEng("boardboard")
                    .nameKor("자유게시판")
                    .menuOrder(1)
                    .description("자유게시판입니다.")
                    .url("freeboard22")
                    .menuType(MenuType.BOARD)
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(MenuErrorCode.DUPLICATED_MENU_NAME_KOR.getMessage());
    }

    @Test
    void 메뉴_부모_성공() {
        //given
        Menu menu = createData("freeboard", "자유게시판", "자유게시판입니다", 1, MenuType.FOLDER, "freeboard");
        //when
        menuCreateUseCase.create(MenuCreateDto.Request.builder()
                .nameEng("freeboard1")
                .nameKor("자유게시판1")
                .menuOrder(1)
                .description("자유게시판입니다.")
                .parentId(menu.getMenuId())
                .url("freeboard1")
                .menuType(MenuType.BOARD)
                .build());
        //then
        Menu created = menuJpaRepository.findByNameEng("freeboard1").get();
        Assertions.assertThat(created.getDescription()).isEqualTo("자유게시판입니다.");
        Assertions.assertThat(created.getNameKor()).isEqualTo("자유게시판1");
        Assertions.assertThat(created.getNameEng()).isEqualTo("freeboard1");
        Assertions.assertThat(created.getMenuOrder()).isEqualTo(1);
        Assertions.assertThat(created.getParent().getMenuId()).isEqualTo(menu.getMenuId());
    }

    @Test
    void 메뉴_상위폴더아님_실패() {
        //given
        Menu menu = createData("freeboard", "자유게시판", "자유게시판입니다", 1, MenuType.BOARD, "freeboard");
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            menuCreateUseCase.create(MenuCreateDto.Request.builder()
                    .nameEng("freeboard1")
                    .nameKor("자유게시판1")
                    .menuOrder(1)
                    .description("자유게시판입니다.")
                    .parentId(menu.getMenuId())
                    .url("freeboard1")
                    .menuType(MenuType.BOARD)
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(MenuErrorCode.ONLY_FOLDER_MENU_CAN_BE_PARENT.getMessage());

    }

    @Test
    void 메뉴_부모_없음_실패() {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            menuCreateUseCase.create(MenuCreateDto.Request.builder()
                    .nameEng("freeboard1")
                    .nameKor("자유게시판1")
                    .menuOrder(1)
                    .menuType(MenuType.BOARD)
                    .description("자유게시판입니다.")
                    .parentId(1L)
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(MenuErrorCode.NO_SUCH_MENU.getMessage());
    }

    @Test
    void 메뉴_이미_존재하는_영문_권한명_실패() {
        //given
        Authority authority = new Authority("freeboard1", "자유게시판2");
        authorityJpaRepository.save(authority);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            menuCreateUseCase.create(MenuCreateDto.Request.builder()
                    .nameEng("freeboard1")
                    .nameKor("자유게시판1")
                    .menuOrder(1)
                    .menuType(MenuType.BOARD)
                    .description("자유게시판입니다.")
                    .url("freeboard1")
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(MenuErrorCode.CAN_NOT_USE_NAME_ENG.getMessage());
    }

    @Test
    void 메뉴_이미_존재하는_한글_권한명_실패() {
        //given
        Authority authority = new Authority("freeboard2", "자유게시판1");
        authorityJpaRepository.save(authority);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            menuCreateUseCase.create(MenuCreateDto.Request.builder()
                    .nameEng("freeboard1")
                    .nameKor("자유게시판1")
                    .menuOrder(1)
                    .menuType(MenuType.BOARD)
                    .description("자유게시판입니다.")
                    .url("freeboard1")
                    .build());
        }).isInstanceOf(BusinessException.class).hasMessage(MenuErrorCode.CAN_NOT_USE_NAME_KOR.getMessage());
    }
}