package com.se.apiserver.v1.authority.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import com.se.apiserver.v1.authority.application.error.AuthorityGroupErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
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

class AuthorityGroupTest {
  private static Validator validator;

  @BeforeAll
  static void setUp(){
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void 제약조건_검사() throws Exception{
    //given
    String name = "1";
    String description = "2";
    AuthorityGroupType type = AuthorityGroupType.NORMAL;
    AuthorityGroup authorityGroup = new AuthorityGroup(name, description, type);
    //when
    Set<ConstraintViolation<AuthorityGroup>> constraintViolations = validator.validate(authorityGroup);
    Iterator<ConstraintViolation<AuthorityGroup>> iterator = constraintViolations.iterator();
    List<String> violationList = new ArrayList<>();
    while(iterator.hasNext()){
      violationList.add(String.valueOf(iterator.next().getInvalidValue()));
    }
    //then
    assertAll(
        () -> assertTrue(violationList.contains(name)),
        () -> assertTrue(violationList.contains(description))
    );
  }

  @Test
  public void 권한그룹_정보갱신_검사() throws Exception{
    //given
    String name = "name_for_update";
    String description = "description_for_update";
    AuthorityGroup authorityGroup = new AuthorityGroup("name", "description", AuthorityGroupType.NORMAL);
    //when
    authorityGroup.updateName(name);
    authorityGroup.updateDescription(description);
    //then
    assertAll(
        () -> assertEquals(name, authorityGroup.getName()),
        () -> assertEquals(description, authorityGroup.getDescription())
    );
  }

  @Test
  public void 권한그룹_삭제_검사_성공() throws Exception{
    //given
    AuthorityGroup authorityGroup = new AuthorityGroup("name", "description", AuthorityGroupType.NORMAL);
    //when
    //then
    assertDoesNotThrow(() -> authorityGroup.remove());
  }

  @Test
  public void 권한그룹_삭제_검사_실패_ANONYMOUS_GROUP() throws Exception{
    //given
    AuthorityGroup authorityGroup = new AuthorityGroup("name", "description", AuthorityGroupType.ANONYMOUS);
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroup.remove());
    //then
    assertEquals(AuthorityGroupErrorCode.CAN_NOT_DELETE_ANONYMOUS_GROUP, exception.getErrorCode());
  }

  @Test
  public void 권한그룹_삭제_검사_실패_DEFAULT_GROUP() throws Exception{
    //given
    AuthorityGroup authorityGroup = new AuthorityGroup("name", "description", AuthorityGroupType.DEFAULT);
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroup.remove());
    //then
    assertEquals(AuthorityGroupErrorCode.CAN_NOT_DELETE_DEFAULT_GROUP, exception.getErrorCode());
  }

  @Test
  public void 권한그룹_삭제_검사_실패_SYSTEM_GROUP() throws Exception{
    //given
    AuthorityGroup authorityGroup = new AuthorityGroup("name", "description", AuthorityGroupType.SYSTEM);
    //when
    BusinessException exception = assertThrows(BusinessException.class, () -> authorityGroup.remove());
    //then
    assertEquals(AuthorityGroupErrorCode.CAN_NOT_DELETE_SYSTEM_GROUP, exception.getErrorCode());
  }
}