package com.se.apiserver.v1.menu.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
  public void 메뉴_정보갱신_검사() throws Exception{
    // given
    
    // when
    
    // then
  }
}