package com.se.apiserver.v1.opensubject.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.opensubject.application.error.OpenSubjectErrorCode;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.opensubject.infra.repository.OpenSubjectJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpenSubjectDeleteService {

  private final OpenSubjectJpaRepository openSubjectJpaRepository;

  @Transactional
  public void delete(Long openSubjectId){
    OpenSubject openSubject = openSubjectJpaRepository
        .findById(openSubjectId)
        .orElseThrow(() ->
            new BusinessException(OpenSubjectErrorCode.NO_SUCH_OPEN_SUBJECT)
        );
    openSubjectJpaRepository.delete(openSubject);
  }
}
