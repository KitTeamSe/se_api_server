package com.se.apiserver.v1.account.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.application.dto.QuestionReadDto;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuestionReadServiceTest {

  @Mock
  private AccountContextService accountContextService;
  @Mock
  private QuestionJpaRepository questionJpaRepository;
  @Mock
  private AccountJpaRepository accountJpaRepository;
  
  @InjectMocks
  private QuestionReadService questionReadService;

  @Test
  public void 모든_질문목록_조회_성공() throws Exception{
    //given
    when(questionJpaRepository.findAll()).thenReturn(Collections.emptyList());
    //when
    //then
    assertDoesNotThrow(() -> questionReadService.readAll());
  }

  @Test
  public void 내_질문조회_성공() throws Exception{
    //given
    when(accountContextService.getContextAccount()).thenReturn(
        Account.builder()
            .question(Question.builder().build())
            .answer("")
            .build()
    );
    //when
    //then
    assertDoesNotThrow(() -> questionReadService.readMyQuestionAndAnswer());
  }

  @Test
  public void 사용자_아이디로_질문조회_성공() {
    //given
    String userId = "newUser00001";
    Question question = Question.builder().text("내 고향은????").build();
    Account account = Account.builder().idString(userId).question(question).build();

    when(accountJpaRepository.findByIdString(userId)).thenReturn(java.util.Optional.ofNullable(account));
    //when
    QuestionReadDto.Response response = questionReadService.readQuestionByUserId(userId);
    //then
    assertEquals(response, QuestionReadDto.Response.fromEntity(question));
  }
}