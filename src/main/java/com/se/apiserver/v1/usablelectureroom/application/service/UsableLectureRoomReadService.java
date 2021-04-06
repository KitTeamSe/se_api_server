package com.se.apiserver.v1.usablelectureroom.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import com.se.apiserver.v1.usablelectureroom.application.dto.UsableLectureRoomReadDto;
import com.se.apiserver.v1.usablelectureroom.application.dto.UsableLectureRoomReadDto.Response;
import com.se.apiserver.v1.usablelectureroom.application.error.UsableLectureRoomErrorCode;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import com.se.apiserver.v1.usablelectureroom.infra.repository.UsableLectureRoomJpaRepository;
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
public class UsableLectureRoomReadService {

  private final UsableLectureRoomJpaRepository usableLectureRoomJpaRepository;
  private final TimeTableJpaRepository timeTableJpaRepository;

  public UsableLectureRoomReadDto.Response read(Long id){
    UsableLectureRoom usableLectureRoom = usableLectureRoomJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(UsableLectureRoomErrorCode.NO_SUCH_USABLE_LECTURE_ROOM));
    return UsableLectureRoomReadDto.Response.fromEntity(usableLectureRoom);
  }

  public PageImpl readAllByTimeTableId(Pageable pageable, Long timeTableId){

    TimeTable timeTable = timeTableJpaRepository
        .findById(timeTableId)
        .orElseThrow(() -> new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE));

    Page<UsableLectureRoom> all = usableLectureRoomJpaRepository
        .findAllByTimeTable(pageable, timeTable);
    List<Response> responseList = all
        .stream()
        .map(t -> UsableLectureRoomReadDto.Response.fromEntity(t))
        .collect(Collectors.toList());
    return new PageImpl(responseList, all.getPageable(), all.getTotalElements());
  }
}
