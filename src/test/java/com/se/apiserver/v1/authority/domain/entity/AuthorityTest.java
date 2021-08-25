package com.se.apiserver.v1.authority.domain.entity;

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


class AuthorityTest {

  private static Validator validator;

  @BeforeAll
  static void setUp(){
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void 제약조건_검사() throws Exception{
    //given
    String nameEng = "1";
    String nameKor = "2";
    Authority authority = new Authority(nameEng, nameKor);
    //when
    Set<ConstraintViolation<Authority>> constraintViolations = validator.validate(authority);
    Iterator<ConstraintViolation<Authority>> iterator = constraintViolations.iterator();
    List<String> violationList = new ArrayList<>();
    while(iterator.hasNext()){
      violationList.add(String.valueOf(iterator.next().getInvalidValue()));
    }
    //then
    assertAll(
        () -> assertTrue(violationList.contains(nameEng)),
        () -> assertTrue(violationList.contains(nameKor))
    );
  }

  @Test
  public void 권한_정보갱신_검사() throws Exception{
    //given
    String nameEng = "nameEng_for_update";
    String nameKor = "nameKor_for_update";
    Authority authority = new Authority("nameEng", "nameKor");
    //when
    authority.updateNameEng(nameEng);
    authority.updateNameKor(nameKor);
    //then
    assertAll(
        () -> assertEquals(nameEng, authority.getNameEng()),
        () -> assertEquals(nameKor, authority.getNameKor())
    );
  }

}