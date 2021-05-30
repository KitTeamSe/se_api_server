package com.se.apiserver.v1.division.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.division.application.error.DivisionErrorCode;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.division.infra.repository.DivisionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DivisionDeleteService {

  private final DivisionJpaRepository divisionJpaRepository;

  @Transactional
  public void delete(Long divisionId){
    Division division = divisionJpaRepository.findById(divisionId)
        .orElseThrow(() -> new BusinessException(DivisionErrorCode.NO_SUCH_DIVISION));

    division.delete();

    divisionJpaRepository.delete(division);
  }
}
