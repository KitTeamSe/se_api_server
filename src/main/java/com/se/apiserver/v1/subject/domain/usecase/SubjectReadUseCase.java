package com.se.apiserver.v1.subject.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.lectureroom.infra.dto.LectureRoomReadDto;
import com.se.apiserver.v1.lectureroom.infra.dto.LectureRoomReadDto.Response;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.error.SubjectErrorCode;
import com.se.apiserver.v1.subject.infra.dto.SubjectReadDto;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubjectReadUseCase {

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
