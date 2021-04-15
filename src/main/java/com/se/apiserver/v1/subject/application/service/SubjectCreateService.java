package com.se.apiserver.v1.subject.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.application.error.SubjectErrorCode;
import com.se.apiserver.v1.subject.application.dto.SubjectCreateDto;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectCreateService {

  private final SubjectJpaRepository subjectJpaRepository;

  @Transactional
  public Long create(SubjectCreateDto.Request request){
    // 교과 코드가 중복되는지 검사
    if(subjectJpaRepository.findByCode(request.getCode()).isPresent())
      throw new BusinessException(SubjectErrorCode.DUPLICATED_SUBJECT);

    Subject subject = new Subject(request.getCurriculum(),
        request.getType(),
        request.getCode(),
        request.getName(),
        request.getGrade(),
        request.getSemester(),
        request.getCredit(),
        false,
        request.getNote());

    return subjectJpaRepository.save(subject).getSubjectId();
  }
}
