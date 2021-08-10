package com.se.apiserver.v1.account.domain.entity;

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

class AccountTest {

  private static Validator validator;

  @BeforeAll
  static void setup(){
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void 제약조건_검사() throws Exception{
    //given
    String idString = "1";
    String name = "2";
    String nickname = "3";
    String studentId = "4";
    String phoneNumber = "5";
    String email = "6";
    String last = "7";
    String answer = "8";

    long accountId = 0L;
    String password = "password";
    AccountType accountType = AccountType.STUDENT;
    InformationOpenAgree info = InformationOpenAgree.AGREE;
    Question question = Question.builder().build();

    Account account = Account.builder()
        .accountId(accountId)
        .idString(idString)
        .password(password)
        .name(name)
        .nickname(nickname)
        .studentId(studentId)
        .type(accountType)
        .phoneNumber(phoneNumber)
        .email(email)
        .lastSignInIp(last)
        .informationOpenAgree(info)
        .question(question)
        .answer(answer)
        .build();
    Set<ConstraintViolation<Account>> constraintViolation = validator.validate(account);
    Iterator<ConstraintViolation<Account>> iterator = constraintViolation.iterator();
    List<String> violationList = new ArrayList<>();
    while(iterator.hasNext()){
      violationList.add(String.valueOf(iterator.next().getInvalidValue()));
    }
    //when
    //then
    assertAll(
        () -> assertTrue(violationList.contains(idString)),
        () -> assertTrue(violationList.contains(name)),
        () -> assertTrue(violationList.contains(nickname)),
        () -> assertTrue(violationList.contains(studentId)),
        () -> assertTrue(violationList.contains(phoneNumber)),
        () -> assertTrue(violationList.contains(email)),
        () -> assertTrue(violationList.contains(last)),
        () -> assertTrue(violationList.contains(answer))
    );
  }

  @Test
  public void 회원_정보갱신_검사() throws Exception{
    //given
    long accountId = 0L;
    String idString = "4글자-20글자";
    String password = "password";
    String name = "2글자-20글자";
    String nickname = "2글자-20글자";
    String studentId = "8글자-20글자";
    AccountType type = AccountType.STUDENT;
    String phoneNumber = "10글자-20글자";
    String email = "test@test.com";
    String ip = "192.168.0.1";
    InformationOpenAgree info = InformationOpenAgree.AGREE;
    Question question = Question.builder().build();
    String answer = "2글자-100글자";
    Account account = Account.builder()
        .accountId(accountId)
        .idString(idString)
        .phoneNumber(phoneNumber)
        .email(email)
        .build();
    //when
    account.updateQnA(question, answer);
    account.updateNickname(nickname);
    account.updateStudentId(studentId);
    account.updatePassword(password);
    account.updateInformationOpenAgree(info);
    account.updateType(type);
    account.updateName(name);
    account.updateLastSignIp(ip);
    //then
    assertAll(
        () -> assertEquals(accountId, account.getAccountId()),
        () -> assertEquals(idString, account.getIdString()),
        () -> assertEquals(password, account.getPassword()),
        () -> assertEquals(name, account.getName()),
        () -> assertEquals(nickname, account.getNickname()),
        () -> assertEquals(studentId, account.getStudentId()),
        () -> assertEquals(type, account.getType()),
        () -> assertEquals(phoneNumber, account.getPhoneNumber()),
        () -> assertEquals(email, account.getEmail()),
        () -> assertEquals(ip, account.getLastSignInIp()),
        () -> assertEquals(info, account.getInformationOpenAgree()),
        () -> assertEquals(question, account.getQuestion()),
        () -> assertEquals(answer, account.getAnswer())
    );
  }
}