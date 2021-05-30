package com.se.apiserver.v1.division.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.division.application.dto.DivisionCreateDto;
import com.se.apiserver.v1.division.application.error.DivisionErrorCode;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.division.infra.repository.DivisionJpaRepository;
import com.se.apiserver.v1.opensubject.application.error.OpenSubjectErrorCode;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.opensubject.infra.repository.OpenSubjectJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DivisionCreateService {

  private final DivisionJpaRepository divisionJpaRepository;
  private final OpenSubjectJpaRepository openSubjectJpaRepository;

  @Transactional
  public Long create(DivisionCreateDto.Request request){
    OpenSubject openSubject = openSubjectJpaRepository.findById(request.getOpenSubjectId())
        .orElseThrow(() -> new BusinessException(OpenSubjectErrorCode.NO_SUCH_OPEN_SUBJECT));

    if(divisionJpaRepository.findByOpenSubjectAndDivisionNumber(openSubject, request.getDivisionNumber()).isPresent())
      throw new BusinessException(DivisionErrorCode.DUPLICATED_DIVISION_NUMBER);

    Division division = new Division(openSubject, request.getDivisionNumber(), 0, false, request.getNote());

    return divisionJpaRepository.save(division).getDivisionId();
  }
}
