package com.se.apiserver.v1.menu.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.application.dto.MenuReadDto;
import com.se.apiserver.v1.menu.application.error.MenuErrorCode;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuReadServiceTest {

  @Mock
  private MenuJpaRepository menuJpaRepository;
  @Mock
  private AccountContextService accountContextService;

  @InjectMocks
  private MenuReadService menuReadService;

  @Test
  public void 메뉴_조회_단일_성공() throws Exception{
    // given
    Long menuId = 1L;
    Menu menu = new Menu("nameEng", "url", "한글이름", 1, "설명", MenuType.FOLDER);
    when(menuJpaRepository.findById(menuId)).thenReturn(Optional.ofNullable(menu));

    // when
    MenuReadDto.ReadResponse response = menuReadService.read(menuId);

    // then
    assertAll(
        () -> assertEquals(menu.getMenuId(), response.getMenuId()),
        () -> assertEquals(menu.getNameEng(), response.getNameEng()),
        () -> assertEquals(menu.getUrl(), response.getUrl()),
        () -> assertEquals(menu.getNameKor(), response.getNameKor())
    );
  }

  @Test
  public void 메뉴_조회_All_성공() throws Exception{
    Set<String> authorities = mock(Set.class);
    List<Menu> menus = new ArrayList<>();
    Menu menu = new Menu("nameEng", "url", "한글이름", 1, "설명", MenuType.FOLDER);
    menus.add(menu);

    when(accountContextService.getContextAuthorities()).thenReturn(authorities);
    when(menuJpaRepository.findAllRootMenu()).thenReturn(menus);
    when(menu.canAccess(authorities)).thenReturn(true);

    List<MenuReadDto.ReadAllResponse> response = menuReadService.readAll();

    assertAll(
        () -> assertEquals(menus.get(0).getMenuId(), response.get(0).getReadResponse().getMenuId()),
        () -> assertEquals(menus.get(0).getNameEng(), response.get(0).getReadResponse().getNameEng()),
        () -> assertEquals(menus.get(0).getUrl(), response.get(0).getReadResponse().getUrl()),
        () -> assertEquals(menus.get(0).getNameKor(), response.get(0).getReadResponse().getNameKor())
    );
  }

  @Test
  public void 존재하지_않는_메뉴_실패() throws Exception{
    // given
    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menuReadService.read(0L));

    // then
    assertEquals(MenuErrorCode.NO_SUCH_MENU, exception.getErrorCode());
  }
}