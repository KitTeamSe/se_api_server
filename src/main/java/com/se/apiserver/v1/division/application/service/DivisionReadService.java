package com.se.apiserver.v1.division.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.division.application.dto.DivisionReadDto;
import com.se.apiserver.v1.division.application.dto.DivisionReadDto.Response;
import com.se.apiserver.v1.division.application.error.DivisionErrorCode;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.division.infra.repository.DivisionJpaRepository;
import com.se.apiserver.v1.opensubject.application.error.OpenSubjectErrorCode;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.opensubject.infra.repository.OpenSubjectJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DivisionReadService {

  private final DivisionJpaRepository divisionJpaRepository;
  private final OpenSubjectJpaRepository openSubjectJpaRepository;

  public DivisionReadDto.Response read(Long id){
    Division division = divisionJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(DivisionErrorCode.NO_SUCH_DIVISION));
    return DivisionReadDto.Response.fromEntity(division);
  }

  public List<DivisionReadDto.Response> readAllByOpenSubjectId(Long openSubjectId){

    OpenSubject openSubject = openSubjectJpaRepository
        .findById(openSubjectId)
        .orElseThrow(() -> new BusinessException(OpenSubjectErrorCode.NO_SUCH_OPEN_SUBJECT));

    return divisionJpaRepository
        .findAllByOpenSubject(openSubject)
        .stream()
        .map(Response::fromEntity)
        .collect(Collectors.toList());
  }
}
