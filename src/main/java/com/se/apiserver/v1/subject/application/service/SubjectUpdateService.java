package com.se.apiserver.v1.subject.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.application.error.SubjectErrorCode;
import com.se.apiserver.v1.subject.application.dto.SubjectReadDto;
import com.se.apiserver.v1.subject.application.dto.SubjectReadDto.Response;
import com.se.apiserver.v1.subject.application.dto.SubjectUpdateDto;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectUpdateService {

  private final SubjectJpaRepository subjectJpaRepository;

  @Transactional
  public Long update(SubjectUpdateDto.Request request){
    Subject subject = subjectJpaRepository
        .findById(request.getSubjectId())
        .orElseThrow(() -> new BusinessException(SubjectErrorCode.NO_SUCH_SUBJECT));

    if(request.getCode() != null){
      Optional<Subject> optionalSubject = subjectJpaRepository.findByCode(request.getCode());
        if(optionalSubject.isPresent() && !optionalSubject.get().getSubjectId().equals(request.getSubjectId()))
          throw new BusinessException(SubjectErrorCode.DUPLICATED_SUBJECT);

      subject.updateCode(request.getCode());
    }

    if(request.getCurriculum() != null)
      subject.updateCurriculum(request.getCurriculum());

    if(request.getType() != null)
      subject.updateType(request.getType());

    if(request.getName() != null)
      subject.updateName(request.getName());

    if(request.getGrade() != null)
      subject.updateGrade(request.getGrade());

    if(request.getSemester() != null)
      subject.updateSemester(request.getSemester());

    if(request.getCredit() != null)
      subject.updateCredit(request.getCredit());

    if(request.getNote() != null){
      String note = request.getNote().isEmpty() ? null : request.getNote();
      subject.updateNote(note);
    }

    return subjectJpaRepository.save(subject).getSubjectId();
  }
}
