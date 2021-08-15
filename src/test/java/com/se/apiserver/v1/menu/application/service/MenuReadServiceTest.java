package com.se.apiserver.v1.menu.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.service.AccountContextService;
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
    Menu menu = mock(Menu.class);

    when(menuJpaRepository.findById(menuId)).thenReturn(Optional.ofNullable(menu));

    // when
    // then
    assertDoesNotThrow(() -> menuReadService.read(menuId));
  }

  @Test
  public void 메뉴_조회_All_성공() throws Exception{
    assertDoesNotThrow(() -> menuReadService.readAll());
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