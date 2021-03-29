package com.se.apiserver.v1.lectureroom.domain.usecase;

import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.common.exception.BusinessException;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.domain.error.LectureRoomErrorCode;
import com.se.apiserver.v1.lectureroom.infra.dto.LectureRoomReadDto;
import com.se.apiserver.v1.lectureroom.infra.dto.LectureRoomReadDto.Response;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
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
public class LectureRoomReadUseCase {

  private final LectureRoomJpaRepository lectureRoomJpaRepository;

  public LectureRoomReadDto.Response read(Long id){
    LectureRoom lectureRoom = lectureRoomJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(LectureRoomErrorCode.NO_SUCH_LECTURE_ROOM));
    return LectureRoomReadDto.Response.fromEntity(lectureRoom);
  }

  public PageImpl readAll(Pageable pageable){
    Page<LectureRoom> all = lectureRoomJpaRepository.findAll(pageable);
    List<LectureRoomReadDto.Response> responseList = all
        .stream()
        .map(lr -> LectureRoomReadDto.Response.fromEntity(lr))
        .collect(Collectors.toList());
    return new PageImpl(responseList, all.getPageable(), all.getTotalElements());
  }
}
