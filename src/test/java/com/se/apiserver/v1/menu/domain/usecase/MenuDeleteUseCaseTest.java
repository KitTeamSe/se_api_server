package com.se.apiserver.v1.menu.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import com.se.apiserver.v1.menu.domain.error.MenuErrorCode;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MenuDeleteUseCaseTest {
    @Autowired
    MenuJpaRepository menuJpaRepository;
    @Autowired
    MenuDeleteUseCase menuDeleteUseCase;

    private Menu.MenuBuilder createData(String nameEng, String nameKor, String description, Integer order, MenuType menuType, String url) {
        Menu.MenuBuilder menuBuilder = Menu.builder()
                .nameEng(nameEng)
                .nameKor(nameKor)
                .description(description)
                .menuOrder(order)
                .menuType(menuType)
                .url(url);

        return menuBuilder;
    }

    @Test
    void 메뉴_삭제_성공() {
        //given
        Menu menu = createData("freeboard1", "자유게시판1", "자유게시판입니다", 2, MenuType.BOARD, "freeboard1").build();
        menuJpaRepository.save(menu);
        Long id = menu.getMenuId();

        //when
        menuDeleteUseCase.delete(menu.getMenuId());
        //then
        Assertions.assertThat(menuJpaRepository.findById(id).isEmpty()).isEqualTo(true);
    }

    @Test
    void 메뉴_삭제_자식있을때_실패() {
        //given
        Menu menu2 = createData("freeboard1", "자유게시판1", "자유게시판입니다", 2, MenuType.FOLDER, "freeboard1").build();
        menuJpaRepository.save(menu2);

        Menu menu = createData("freeboard", "자유게시판", "자유게시판입니다", 1, MenuType.BOARD, "freeboard2").build();
        menu.updateParent(menu2);
        menuJpaRepository.save(menu);
        //when
        Assertions.assertThatThrownBy(() -> {
            menuDeleteUseCase.delete(menu2.getMenuId());
        }).isInstanceOf(BusinessException.class).hasMessage(MenuErrorCode.CHILD_REMOVE_FIRST.getMessage());
    }
}