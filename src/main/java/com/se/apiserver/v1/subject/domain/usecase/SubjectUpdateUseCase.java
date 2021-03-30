package com.se.apiserver.v1.subject.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.error.SubjectErrorCode;
import com.se.apiserver.v1.subject.infra.dto.SubjectReadDto;
import com.se.apiserver.v1.subject.infra.dto.SubjectReadDto.Response;
import com.se.apiserver.v1.subject.infra.dto.SubjectUpdateDto;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectUpdateUseCase {

  private final SubjectJpaRepository subjectJpaRepository;

  @Transactional
  public SubjectReadDto.Response update(SubjectUpdateDto.Request request){
    Subject subject = subjectJpaRepository
        .findById(request.getSubjectId())
        .orElseThrow(() -> new BusinessException(SubjectErrorCode.NO_SUCH_SUBJECT));

    if(request.getCode() != null){
      if(subjectJpaRepository.findByCode(request.getCode()).isPresent())
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

    subjectJpaRepository.save(subject);

    return Response.fromEntity(subject);
  }
}
