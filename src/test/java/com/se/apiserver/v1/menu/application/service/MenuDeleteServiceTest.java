package com.se.apiserver.v1.menu.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.application.error.MenuErrorCode;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.infra.repository.MenuJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuDeleteServiceTest {

  @Mock
  private MenuJpaRepository menuJpaRepository;

  @InjectMocks
  private MenuDeleteService menuDeleteService;

  @Test
  public void 메뉴_삭제_성공 () throws Exception{
    // given
    Menu menu = mock(Menu.class);
    when(menu.isRemovable()).thenReturn(true);
    when(menuJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(menu));

    // when
    // then
    assertDoesNotThrow(() -> menuDeleteService.delete(1L));
  }

  @Test
  public void 존재하지_않는_메뉴_실패() throws Exception{
    // given
    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menuDeleteService.delete(1L));
    // then
    assertEquals(MenuErrorCode.NO_SUCH_MENU, exception.getErrorCode());
  }

  @Test
  public void 자식메뉴_존재_삭제실패 () throws Exception{
    // given
    Menu menu = mock(Menu.class);
    when(menuJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(menu));

    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menuDeleteService.delete(1L));

    // then
    assertEquals(MenuErrorCode.CHILD_REMOVE_FIRST, exception.getErrorCode());
  }
}