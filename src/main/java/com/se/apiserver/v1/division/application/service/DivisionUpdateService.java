package com.se.apiserver.v1.division.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.division.application.dto.DivisionUpdateDto;
import com.se.apiserver.v1.division.application.error.DivisionErrorCode;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.division.infra.repository.DivisionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DivisionUpdateService {

  private final DivisionJpaRepository divisionJpaRepository;

  @Transactional
  public Long update(DivisionUpdateDto.Request request){
    Division division = divisionJpaRepository.findById(
        request.getDivisionId()).orElseThrow(() -> {throw new BusinessException(DivisionErrorCode.NO_SUCH_DIVISION);});

    updateDivisionNumber(division, request.getDivisionNumber());

    updateNote(division, request.getNote());

    return divisionJpaRepository.save(division).getDivisionId();
  }

  private void updateDivisionNumber(Division division, Integer divisionNumber){
    if(divisionNumber == null)
      return;

    if(division.getDivisionNumber().equals(divisionNumber))
      return;

    if(divisionJpaRepository.findByOpenSubjectAndDivisionNumber(division.getOpenSubject(), divisionNumber).isPresent())
      throw new BusinessException(DivisionErrorCode.DUPLICATED_DIVISION_NUMBER);

    division.updateDivisionNumber(divisionNumber);
  }

  private void updateNote(Division division, String note){
    if(note == null)
      return;

    division.updateNote(note);
  }
}
