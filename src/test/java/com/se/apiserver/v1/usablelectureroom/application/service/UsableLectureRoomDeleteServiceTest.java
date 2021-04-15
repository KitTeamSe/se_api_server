package com.se.apiserver.v1.usablelectureroom.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import com.se.apiserver.v1.timetable.application.service.TimeTableCreateServiceTest;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import com.se.apiserver.v1.usablelectureroom.application.error.UsableLectureRoomErrorCode;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import com.se.apiserver.v1.usablelectureroom.infra.repository.UsableLectureRoomJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UsableLectureRoomDeleteServiceTest {

  @Autowired
  UsableLectureRoomJpaRepository usableLectureRoomJpaRepository;

  @Autowired
  UsableLectureRoomDeleteService usableLectureRoomDeleteService;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  LectureRoomJpaRepository lectureRoomJpaRepository;

  @Test
  void 사용_가능_강의실_삭제_성공(){
    // Given
    TimeTable timeTable = TimeTableCreateServiceTest.createTimeTable(timeTableJpaRepository, "테스트 시간표 1");
    LectureRoom lectureRoom = createLectureRoom("XDA", 101);

    UsableLectureRoom usableLectureRoom = usableLectureRoomJpaRepository.save(UsableLectureRoom.builder()
        .timeTable(timeTable)
        .lectureRoom(lectureRoom)
        .build());

    Long id = usableLectureRoom.getUsableLectureRoomId();

    // When
    usableLectureRoomDeleteService.delete(id);

    // Then
    Assertions.assertThat(usableLectureRoomJpaRepository.findById(id).isEmpty()).isEqualTo(true);
  }

  @Test
  void 사용_가능_강의실_삭제_실패(){
    // Given
    Long id = 7777L;

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      usableLectureRoomDeleteService.delete(id);
    }).isInstanceOf(BusinessException.class).hasMessage(UsableLectureRoomErrorCode.NO_SUCH_USABLE_LECTURE_ROOM.getMessage());
  }

  private LectureRoom createLectureRoom(String building, Integer roomNumber){
    return lectureRoomJpaRepository.save(LectureRoom.builder()
        .building(building)
        .roomNumber(roomNumber)
        .capacity(50)
        .build());
  }
}
