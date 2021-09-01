package com.se.apiserver.v1.menu.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.infra.repository.AuthorityJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.application.dto.MenuUpdateDto;
import com.se.apiserver.v1.menu.application.dto.MenuUpdateDto.Request;
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
class MenuUpdateServiceTest {

  @Mock
  private MenuJpaRepository menuJpaRepository;
  @Mock
  private AuthorityJpaRepository authorityJpaRepository;

  @InjectMocks
  private MenuUpdateService menuUpdateService;

  private MenuUpdateDto.Request createRequestEntityWithParentId(Long parentId) {
    return MenuUpdateDto.Request.builder()
        .menuId(2L)
        .nameEng("new engName")
        .nameKor("새 한글이름")
        .url("new url")
        .menuOrder(10)
        .description("새 설명")
        .parentId(parentId)
        .build();
  }

  private Menu createMenuEntity() {
    return new Menu("nameEng", "url", "한글이름", 1, "설명", MenuType.FOLDER);
  }

  @Test
  public void 변경_없는_수정_성공 () throws Exception{
    // given
    Menu menu = createMenuEntity();
    MenuUpdateDto.Request request = Request.builder().menuId(2L).build();

    when(menuJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(menu));
    // when
    // then
    assertDoesNotThrow(() -> menuUpdateService.update(request));

  }

  @Test
  public void 부모_없는_메뉴_수정_성공() throws Exception{
    // given
    Long parentId = null;

    Menu menu = createMenuEntity();
    MenuUpdateDto.Request request = createRequestEntityWithParentId(parentId);

    when(menuJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(menu));

    // when
    // then
    assertDoesNotThrow(() -> menuUpdateService.update(request));
  }

  @Test
  public void 부모_있는_메뉴_수정_성공() throws Exception{
    // given
    Long parentId = 1L;
    MenuUpdateDto.Request request = createRequestEntityWithParentId(parentId);
    Menu menu = createMenuEntity();
    Menu parent = createMenuEntity();

    when(menuJpaRepository.findById(request.getMenuId())).thenReturn(Optional.ofNullable(menu));
    when(menuJpaRepository.findById(parentId)).thenReturn(Optional.ofNullable(parent));
    // when
    // then
    assertDoesNotThrow(() -> menuUpdateService.update(request));
  }

  @Test
  public void 존재하지_않는_메뉴_아이디_실패() throws Exception{
    // given
    MenuUpdateDto.Request request = MenuUpdateDto.Request.builder().menuId(0L).build();
    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menuUpdateService.update(request));
    // then
    assertEquals(MenuErrorCode.NO_SUCH_MENU, exception.getErrorCode());
  }

  @Test
  public void 중복된_한글_이름의_메뉴_실패() throws Exception{
    // given
    Menu menu = createMenuEntity();
    MenuUpdateDto.Request request = createRequestEntityWithParentId(null);

    when(menuJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(menu));
    when(menuJpaRepository.findByNameKor(anyString())).thenReturn(Optional.ofNullable(menu));

    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menuUpdateService.update(request));

    // then
    assertEquals(MenuErrorCode.DUPLICATED_MENU_NAME_KOR, exception.getErrorCode());
  }
  
  @Test
  public void 중복된_한글_이름의_권한_실패() throws Exception{
    // given
    Menu menu = createMenuEntity();
    Authority authority = mock(Authority.class);
    MenuUpdateDto.Request request = createRequestEntityWithParentId(null);

    when(menuJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(menu));
    when(authorityJpaRepository.findByNameKor(anyString())).thenReturn(Optional.ofNullable(authority));

    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menuUpdateService.update(request));

    // then
    assertEquals(MenuErrorCode.CAN_NOT_USE_NAME_KOR, exception.getErrorCode());
  }

  @Test
  public void 중복된_영어_이름의_메뉴_실패() throws Exception{
    // given
    Menu menu = createMenuEntity();
    MenuUpdateDto.Request request = createRequestEntityWithParentId(null);

    when(menuJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(menu));
    when(menuJpaRepository.findByNameEng(anyString())).thenReturn(Optional.ofNullable(menu));

    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menuUpdateService.update(request));

    // then
    assertEquals(MenuErrorCode.DUPLICATED_MENU_NAME_ENG, exception.getErrorCode());
  }

  @Test
  public void 중복된_영어_이름의_권한_실패() throws Exception{
    // given
    Menu menu = createMenuEntity();
    Authority authority = mock(Authority.class);
    MenuUpdateDto.Request request = createRequestEntityWithParentId(null);

    when(menuJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(menu));
    when(authorityJpaRepository.findByNameEng(anyString())).thenReturn(Optional.ofNullable(authority));

    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menuUpdateService.update(request));

    // then
    assertEquals(MenuErrorCode.CAN_NOT_USE_NAME_ENG, exception.getErrorCode());
  }

  @Test
  public void 중복된_URL_실패() throws Exception{
    // given
    Menu menu = createMenuEntity();
    MenuUpdateDto.Request request = createRequestEntityWithParentId(null);

    when(menuJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(menu));
    when(menuJpaRepository.findByUrl(anyString())).thenReturn(Optional.ofNullable(menu));

    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menuUpdateService.update(request));

    // then
    assertEquals(MenuErrorCode.DUPLICATED_URL, exception.getErrorCode());
  }
}