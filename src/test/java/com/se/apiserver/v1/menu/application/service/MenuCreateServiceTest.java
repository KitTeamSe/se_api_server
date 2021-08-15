package com.se.apiserver.v1.menu.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.application.dto.MenuCreateDto;
import com.se.apiserver.v1.menu.application.error.MenuErrorCode;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuCreateServiceTest {

  @Mock
  private MenuJpaRepository menuJpaRepository;
  @Mock
  private AuthorityJpaRepository authorityJpaRepository;

  @InjectMocks
  private MenuCreateService menuCreateService;

  private MenuCreateDto.Request createRequestEntityWithParentId(Long parentId) {
    return MenuCreateDto.Request.builder()
        .nameEng("engName")
        .nameKor("한글명")
        .url("url")
        .menuOrder(2)
        .description("설명")
        .menuType(MenuType.FOLDER)
        .parentId(parentId)
        .build();
  }

  private MenuCreateDto.Request createRequestEntityWithMenuType(MenuType menuType) {
    return MenuCreateDto.Request.builder()
        .nameEng("engName")
        .nameKor("한글명")
        .url("url")
        .menuOrder(2)
        .description("설명")
        .menuType(menuType)
        .parentId(null)
        .build();
  }


  @Test
  public void 부모_없는_메뉴_생성_성공() throws Exception{
    // given
    Long parentId = null;
    MenuCreateDto.Request request = createRequestEntityWithParentId(parentId);

    // when
    // then
    assertDoesNotThrow(() -> menuCreateService.create(request));
  }

  @Test
  public void 부모_있는_메뉴_생성_성공() throws Exception{
    // given
    Long parentId = 1L;
    Menu parent = new Menu("nameEng", "url", "nameKor", 1, "부모설명", MenuType.FOLDER);
    MenuCreateDto.Request request = createRequestEntityWithParentId(parentId);
    when(menuJpaRepository.findById(request.getParentId())).thenReturn(Optional.ofNullable(parent));

    // when
    // then
    assertDoesNotThrow(() -> menuCreateService.create(request));
  }

  @Test
  public void 중복된_한글_이름의_메뉴_실패 () throws Exception{
    // given
    Menu menu = mock(Menu.class);
    MenuCreateDto.Request request = createRequestEntityWithParentId(null);
    when(menuJpaRepository.findByNameKor(request.getNameKor())).thenReturn(Optional.ofNullable(menu));

    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menuCreateService.create(request));

    // then
    assertEquals(MenuErrorCode.DUPLICATED_MENU_NAME_KOR, exception.getErrorCode());
  }

  @Test
  public void 중복된_한글_이름의_권한_실패 () throws Exception{
    // given
    Authority authority = mock(Authority.class);
    MenuCreateDto.Request request = createRequestEntityWithParentId(null);
    when(authorityJpaRepository.findByNameKor(request.getNameKor())).thenReturn(Optional.ofNullable(authority));

    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menuCreateService.create(request));

    // then
    assertEquals(MenuErrorCode.CAN_NOT_USE_NAME_KOR, exception.getErrorCode());
  }

  @Test
  public void 중복된_영어_이름의_메뉴_실패() throws Exception{
    // given
    Menu menu = mock(Menu.class);
    MenuCreateDto.Request request = createRequestEntityWithParentId(null);
    when(menuJpaRepository.findByNameEng(request.getNameEng())).thenReturn(Optional.ofNullable(menu));

    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menuCreateService.create(request));

    // then
    assertEquals(MenuErrorCode.DUPLICATED_MENU_NAME_ENG, exception.getErrorCode());
  }

  @Test
  public void 중복된_영어_이름의_권한_실패() throws Exception{
    // given
    Authority authority = mock(Authority.class);
    MenuCreateDto.Request request = createRequestEntityWithParentId(null);
    when(authorityJpaRepository.findByNameEng(request.getNameEng())).thenReturn(Optional.ofNullable(authority));

    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menuCreateService.create(request));

    // then
    assertEquals(MenuErrorCode.CAN_NOT_USE_NAME_ENG, exception.getErrorCode());
  }

  @Test
  public void 중복된_URL_실패() throws Exception{
    // given
    Menu menu = mock(Menu.class);
    MenuCreateDto.Request request = createRequestEntityWithParentId(null);
    when(menuJpaRepository.findByUrl(request.getUrl())).thenReturn(Optional.ofNullable(menu));

    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menuCreateService.create(request));

    // then
    assertEquals(MenuErrorCode.DUPLICATED_URL, exception.getErrorCode());
  }

  @Test
  public void 부적합한_메뉴타입_실패 () throws Exception{
    // given
    MenuType menuType = MenuType.BOARD;
    MenuCreateDto.Request request = createRequestEntityWithMenuType(menuType);

    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menuCreateService.create(request));

    // then
    assertEquals(MenuErrorCode.CAN_NOT_CREATE_BOARD_MENU_INDEPENDENTLY, exception.getErrorCode());
  }

}