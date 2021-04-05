package com.se.apiserver.v1.subject.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.application.error.SubjectErrorCode;
import com.se.apiserver.v1.subject.application.dto.SubjectReadDto;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
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
public class SubjectReadService {

  private final SubjectJpaRepository subjectJpaRepository;

  public SubjectReadDto.Response read(Long id){
    Subject subject = subjectJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(SubjectErrorCode.NO_SUCH_SUBJECT));
    return SubjectReadDto.Response.fromEntity(subject);
  }

  public PageImpl readAll(Pageable pageable){
    Page<Subject> all = subjectJpaRepository.findAll(pageable);
    List<SubjectReadDto.Response> responseList = all
        .stream()
        .map(s -> SubjectReadDto.Response.fromEntity(s))
        .collect(Collectors.toList());
    return new PageImpl(responseList, all.getPageable(), all.getTotalElements());
  }

}
