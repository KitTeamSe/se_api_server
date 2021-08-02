package com.se.apiserver.v1.account.application.service;

import com.se.apiserver.v1.account.application.dto.QuestionReadDto;
import com.se.apiserver.v1.account.application.dto.QuestionReadDto.Response;
import com.se.apiserver.v1.account.application.dto.QuestionReadDto.ResponseWithAnswer;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionReadService {

  private final AccountContextService accountContextService;
  private final QuestionJpaRepository questionJpaRepository;

  public List<QuestionReadDto.Response> readAll(){
    return questionJpaRepository.findAll().stream().map(
        Response::fromEntity).collect(Collectors.toList());
  }

  public ResponseWithAnswer readMyQuestionAndAnswer() {
    Account account = accountContextService.getContextAccount();
    return ResponseWithAnswer.fromEntity(account.getQuestion(), account.getAnswer());
  }
}
