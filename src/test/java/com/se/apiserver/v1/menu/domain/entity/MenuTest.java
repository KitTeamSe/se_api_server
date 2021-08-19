package com.se.apiserver.v1.menu.domain.entity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.menu.application.error.MenuErrorCode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

class MenuTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void 메뉴_제약조건_검사() throws Exception{
    // given
      // invalid
    String nameEng = "a";
    String nameKor = "a";
    String url = "b";
    Integer menuOrder = 1;
    String description = "a";
    MenuType menuType = MenuType.FOLDER;

    Menu menu = new Menu(nameEng, url, nameKor, menuOrder, description, menuType);

    Set<ConstraintViolation<Menu>> constraintViolations = validator.validate(menu);
    Iterator<ConstraintViolation<Menu>> iterator = constraintViolations.iterator();
    List<String> violationList = new ArrayList<>();
    while(iterator.hasNext()) {
      violationList.add(String.valueOf(iterator.next().getInvalidValue()));
    }
    // when
    // then
    assertAll(
        () -> assertTrue(violationList.contains(nameEng)),
        () -> assertTrue(violationList.contains(url)),
        () -> assertTrue(violationList.contains(nameKor)),
        () -> assertTrue(violationList.contains(description))
    );
  }
  
  @Test
  public void 메뉴_정보갱신_및_권한_검사() throws Exception{
    // given
    Menu menu = new Menu("nameEng2", "url2", "한글이름2", 2, "설명2", MenuType.FOLDER);
      // valid
    Menu parent = new Menu("nameEng1", "url1", "한글이름1", 1, "설명1", MenuType.FOLDER);
    String nameEng = "2-20자";
    String nameKor = "2-20자";
    String url = "2-255자";
    Integer menuOrder = 3;
    String description = "2-255자";

    // when
    menu.updateParent(parent);
    menu.updateNameEng(nameEng);
    menu.updateNameKor(nameKor);
    menu.updateDescription(description);
    menu.updateMenuOrder(menuOrder);
    menu.updateUrl(url);

    // then
    assertAll(
        () -> assertEquals(menu.getParent(), parent),
        () -> assertEquals(menu.getNameEng(), nameEng),
        () -> assertEquals(menu.getNameKor(), nameKor),
        () -> assertEquals(menu.getDescription(), description),
        () -> assertEquals(menu.getMenuOrder(), menuOrder),
        () -> assertEquals(menu.getUrl(), url),

        () -> assertEquals(menu.getAccessAuthority().getNameKor(), nameKor+"_접근"),
        () -> assertEquals(menu.getAccessAuthority().getNameEng(), nameEng+"_ACCESS"),
        () -> assertEquals(menu.getManageAuthority().getNameKor(), nameKor+"_관리"),
        () -> assertEquals(menu.getManageAuthority().getNameEng(), nameEng+"_MANAGE")
    );
  }

  @Test
  public void 부모타입이_FOLDER가_아님 () throws Exception{
    // given
    Menu parent = new Menu("nameEng1", "url1", "한글이름1", 1, "설명1", MenuType.REDIRECT);
    Menu menu = new Menu("nameEng2", "url2", "한글이름2", 2, "설명2", MenuType.FOLDER);

    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> menu.updateParent(parent));
    // then
    assertEquals(MenuErrorCode.ONLY_FOLDER_MENU_CAN_BE_PARENT, exception.getErrorCode());
  }

  @Test
  public void 메뉴_순환구조_발생 () throws Exception{
    // given
    Menu menu1 = new Menu("nameEng1", "url1", "한글이름1", 1, "설명1", MenuType.FOLDER);
    Menu menu2 = new Menu("nameEng2", "url2", "한글이름2", 2, "설명2", MenuType.FOLDER);

    // when
    BusinessException exception = assertThrows(BusinessException.class, () -> {
      menu1.updateParent(menu2);
      menu2.updateParent(menu1);
    });

    // then
    assertEquals(MenuErrorCode.OCCUR_CYCLE, exception.getErrorCode());
  }

  @Test
  public void 메뉴_접근권한_없음 () throws Exception{
    // given
    Set<String> authorities = Collections.emptySet();
    Menu menu = new Menu("nameEng", "url", "한글이름", 1, "설명", MenuType.FOLDER);
    // when
    AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> menu.validateAccessAuthority(authorities));
    // then
    assertEquals("접근 권한이 없습니다", exception.getMessage());
  }

  @Test
  public void 메뉴_관리권한_없음 () throws Exception{
    // given
    Set<String> authorities = Collections.emptySet();
    Menu menu = new Menu("nameEng", "url", "한글이름", 1, "설명", MenuType.FOLDER);
    // when
    AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> menu.validateManageAuthority(authorities));
    // then
    assertEquals("관리 권한이 없습니다", exception.getMessage());
  }
}