package com.se.apiserver.v1.teacher.domain.usecase.participatedteacher;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.teacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.teacher.domain.error.ParticipatedTeacherErrorCode;
import com.se.apiserver.v1.teacher.infra.dto.participatedteacher.ParticipatedTeacherReadDto;
import com.se.apiserver.v1.teacher.infra.dto.participatedteacher.ParticipatedTeacherReadDto.Response;
import com.se.apiserver.v1.teacher.infra.repository.ParticipatedTeacherJpaRepository;
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
public class ParticipatedTeacherReadUseCase {

  private final ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;

  public ParticipatedTeacherReadDto.Response read(Long id){
    ParticipatedTeacher participatedTeacher = participatedTeacherJpaRepository.
        findById(id)
        .orElseThrow(() -> new BusinessException(ParticipatedTeacherErrorCode.NO_SUCH_PARTICIPATED_TEACHER));
    return ParticipatedTeacherReadDto.Response.fromEntity(participatedTeacher);
  }

  public PageImpl readAll(Pageable pageable){
    Page<ParticipatedTeacher> all = participatedTeacherJpaRepository.findAll(pageable);
    List<Response> responseList = all
        .stream()
        .map(t -> ParticipatedTeacherReadDto.Response.fromEntity(t))
        .collect(Collectors.toList());
    return new PageImpl(responseList, all.getPageable(), all.getTotalElements());
  }
}
