package com.se.apiserver.v1.lectureunabletime.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureunabletime.application.dto.LectureUnableTimeReadDto;
import com.se.apiserver.v1.lectureunabletime.application.dto.LectureUnableTimeReadDto.Response;
import com.se.apiserver.v1.lectureunabletime.application.error.LectureUnableTimeErrorCode;
import com.se.apiserver.v1.lectureunabletime.domain.entity.LectureUnableTime;
import com.se.apiserver.v1.lectureunabletime.infra.repository.LectureUnableTimeJpaRepository;
import com.se.apiserver.v1.participatedteacher.application.error.ParticipatedTeacherErrorCode;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.participatedteacher.infra.repository.ParticipatedTeacherJpaRepository;
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
public class LectureUnableTimeReadService {

  private final LectureUnableTimeJpaRepository lectureUnableTimeJpaRepository;
  private final ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;

  public LectureUnableTimeReadDto.Response read(Long id){
    LectureUnableTime lectureUnableTime = lectureUnableTimeJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(LectureUnableTimeErrorCode.NO_SUCH_LECTURE_UNABLE_TIME));
    return LectureUnableTimeReadDto.Response.fromEntity(lectureUnableTime);
  }

  public PageImpl readAllByParticipatedTeacher(Pageable pageable, Long participatedTeacherId){

    ParticipatedTeacher participatedTeacher = participatedTeacherJpaRepository
        .findById(participatedTeacherId)
        .orElseThrow(() -> new BusinessException(ParticipatedTeacherErrorCode.NO_SUCH_PARTICIPATED_TEACHER));

    Page<LectureUnableTime> all = lectureUnableTimeJpaRepository.findAllByParticipatedTeacher(pageable, participatedTeacher);

    List<Response> responseList = all
        .stream()
        .map(lut -> LectureUnableTimeReadDto.Response.fromEntity(lut))
        .collect(Collectors.toList());

    return new PageImpl(responseList, all.getPageable(), all.getTotalElements());
  }
}
