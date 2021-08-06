package com.se.apiserver.v1.account.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.Question;
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
  
  @InjectMocks
  private QuestionReadService questionReadService;

  @Test
  public void 질문조회_성공_ALL() throws Exception{
    //given
    when(questionJpaRepository.findAll()).thenReturn(Collections.emptyList());
    //when
    //then
    assertDoesNotThrow(() -> questionReadService.readAll());
  }

  @Test
  public void 질문조회_성공_MINE() throws Exception{
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
}