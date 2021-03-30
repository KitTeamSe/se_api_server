package com.se.apiserver.v1.subject.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.error.SubjectErrorCode;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectDeleteUseCase {

  private final SubjectJpaRepository subjectJpaRepository;

  @Transactional
  public void delete(Long subjectId){
    Subject subject = subjectJpaRepository
        .findById(subjectId)
        .orElseThrow(() -> new BusinessException(SubjectErrorCode.NO_SUCH_SUBJECT));

    subjectJpaRepository.delete(subject);
  }
}
