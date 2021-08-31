package com.se.apiserver.v1.blacklist.domain.entity;

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

class BlacklistTest {
  private static Validator validator;

  @BeforeAll
  static void setup(){
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void 제약조건_검사() throws Exception{
    //given
    String ip = "1";
    String reason = "2";

    Blacklist blacklist = new Blacklist(ip, reason);
    //when
    Set<ConstraintViolation<Blacklist>> constraintViolation = validator.validate(blacklist);
    Iterator<ConstraintViolation<Blacklist>> iterator = constraintViolation.iterator();
    List<String> violationList = new ArrayList<>();
    while(iterator.hasNext()){
      violationList.add(String.valueOf(iterator.next().getInvalidValue()));
    }
    //then
    assertAll(
        () -> assertTrue(violationList.contains(ip)),
        () -> assertTrue(violationList.contains(reason))
    );
  }
}