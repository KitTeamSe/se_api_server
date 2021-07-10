package com.se.apiserver.v1.usablelectureroom.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.lectureroom.application.service.LectureRoomCreateServiceTest;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import com.se.apiserver.v1.timetable.application.service.TimeTableCreateServiceTest;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import com.se.apiserver.v1.usablelectureroom.application.dto.UsableLectureRoomReadDto;
import com.se.apiserver.v1.usablelectureroom.application.error.UsableLectureRoomErrorCode;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import com.se.apiserver.v1.usablelectureroom.infra.repository.UsableLectureRoomJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UsableLectureRoomReadServiceTest {

  @Autowired
  UsableLectureRoomJpaRepository usableLectureRoomJpaRepository;

  @Autowired
  UsableLectureRoomReadService usableLectureRoomReadService;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  LectureRoomJpaRepository lectureRoomJpaRepository;

  @Test
  void 사용_가능_강의실_조회_성공(){
    // Given
    TimeTable timeTable = TimeTableCreateServiceTest.createTimeTable(timeTableJpaRepository, "테스트 시간표 1");
    LectureRoom lectureRoom = LectureRoomCreateServiceTest
        .createLectureRoom(lectureRoomJpaRepository, "A", 107);
    UsableLectureRoom usableLectureRoom = UsableLectureRoomCreateServiceTest
        .createUsableLectureRoom(usableLectureRoomJpaRepository, timeTable, lectureRoom);

    // When
    UsableLectureRoomReadDto.Response response = usableLectureRoomReadService.read(usableLectureRoom.getUsableLectureRoomId());

    // Then
    Assertions.assertThat(timeTable.getTimeTableId()).isEqualTo(response.getTimeTableId());
    Assertions.assertThat(lectureRoom.getLectureRoomId()).isEqualTo(response.getLectureRoomId());
  }

  @Test
  void 사용_가능_강의실_조회_실패(){
    // Given
    Long id = 99999L;

    // When
    // Then
    Assertions.assertThatThrownBy(() -> {
      usableLectureRoomReadService.read(id);
    }).isInstanceOf(BusinessException.class).hasMessage(UsableLectureRoomErrorCode.NO_SUCH_USABLE_LECTURE_ROOM.getMessage());
  }

  @Test
  void 사용_가능_강의실_전체_조회_성공(){
    // Given
    TimeTable timeTable = TimeTableCreateServiceTest.createTimeTable(timeTableJpaRepository, "테스트 시간표 1");
    LectureRoom lectureRoom = LectureRoomCreateServiceTest
        .createLectureRoom(lectureRoomJpaRepository, "D", 107);
    UsableLectureRoomCreateServiceTest
        .createUsableLectureRoom(usableLectureRoomJpaRepository, timeTable, lectureRoom);

    LectureRoom lectureRoom2 = LectureRoomCreateServiceTest
        .createLectureRoom(lectureRoomJpaRepository, "G", 108);
    UsableLectureRoom usableLectureRoom = UsableLectureRoomCreateServiceTest
        .createUsableLectureRoom(usableLectureRoomJpaRepository, timeTable, lectureRoom2);

    // When
    PageImpl responses = usableLectureRoomReadService.readAllByTimeTableId(PageRequest.builder()
        .size(100)
        .direction(Direction.ASC)
        .page(1)
        .build().of(), timeTable.getTimeTableId());

    // Then
    Assertions.assertThat(responses.getTotalElements()).isEqualTo(2);
  }
}
