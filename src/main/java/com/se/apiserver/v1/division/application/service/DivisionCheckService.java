package com.se.apiserver.v1.division.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.division.application.dto.DivisionCheckDto;
import com.se.apiserver.v1.division.application.error.DivisionErrorCode;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.division.infra.repository.DivisionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DivisionCheckService {

  private final DivisionJpaRepository divisionJpaRepository;

  public DivisionCheckDto.Response check(Long id){
    Division division = divisionJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(DivisionErrorCode.NO_SUCH_DIVISION));

    DivisionErrorCode errorCode = null;

    int deployedTeachingTime = division.getDeployedTeachingTime();
    int teachingTimePerWeek = division.getOpenSubject().getTeachingTimePerWeek();

    if(deployedTeachingTime < teachingTimePerWeek)
      errorCode = DivisionErrorCode.LESS_DEPLOYED_TEACHING_TIME;
    else if(deployedTeachingTime > teachingTimePerWeek)
      errorCode = DivisionErrorCode.EXCEEDED_DEPLOYED_TEACHING_TIME;

    return DivisionCheckDto.Response.builder()
        .divisionId(division.getDivisionId())
        .divisionNumber(division.getDivisionNumber())
        .isCompleted(errorCode == null)
        .divisionErrorCode(errorCode)
        .deployedTeachingTime(deployedTeachingTime)
        .teachingTimePerWeek(teachingTimePerWeek)
        .build();
  }
}
