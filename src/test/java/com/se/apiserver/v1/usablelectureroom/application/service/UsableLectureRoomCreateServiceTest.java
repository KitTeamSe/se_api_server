package com.se.apiserver.v1.usablelectureroom.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureroom.application.error.LectureRoomErrorCode;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.application.service.TimeTableCreateServiceTest;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import com.se.apiserver.v1.usablelectureroom.application.dto.UsableLectureRoomCreateDto;
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
public class UsableLectureRoomCreateServiceTest {

  @Autowired
  UsableLectureRoomJpaRepository usableLectureRoomJpaRepository;

  @Autowired
  UsableLectureRoomCreateService usableLectureRoomCreateService;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  LectureRoomJpaRepository lectureRoomJpaRepository;

  @Test
  void 사용_가능_강의실_생성_성공(){
    // Given
    TimeTable timeTable = TimeTableCreateServiceTest.createTimeTable(timeTableJpaRepository, "테스트 시간표 1");

    LectureRoom lectureRoom = createLectureRoom("D", 107);

    UsableLectureRoomCreateDto.Request request = UsableLectureRoomCreateDto.Request.builder()
        .timeTableId(timeTable.getTimeTableId())
        .lectureRoomId(lectureRoom.getLectureRoomId())
        .build();

    // When
    Long id = usableLectureRoomCreateService.create(request);

    // Then
    Assertions.assertThat(usableLectureRoomJpaRepository.findById(id).isPresent()).isEqualTo(true);
  }

  @Test
  void 사용_가능_강의실_생성_강의실_이미_사용중_실패(){
    // Given
    TimeTable timeTable = TimeTableCreateServiceTest.createTimeTable(timeTableJpaRepository, "테스트 시간표 1");

    LectureRoom lectureRoom = createLectureRoom("D", 108);

    usableLectureRoomJpaRepository.save(UsableLectureRoom.builder()
        .timeTable(timeTable)
        .lectureRoom(lectureRoom)
        .build());

    UsableLectureRoomCreateDto.Request request = UsableLectureRoomCreateDto.Request.builder()
        .timeTableId(timeTable.getTimeTableId())
        .lectureRoomId(lectureRoom.getLectureRoomId())
        .build();

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      usableLectureRoomCreateService.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(UsableLectureRoomErrorCode.DUPLICATED_USABLE_LECTURE_ROOM.getMessage());
  }

  @Test
  void 사용_가능_강의실_생성_존재하지_않는_시간표_실패(){
    // Given
    LectureRoom lectureRoom = createLectureRoom("D", 109);

    Long timeTableId = 17777L;

    UsableLectureRoomCreateDto.Request request = UsableLectureRoomCreateDto.Request.builder()
        .timeTableId(timeTableId)
        .lectureRoomId(lectureRoom.getLectureRoomId())
        .build();

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      usableLectureRoomCreateService.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(TimeTableErrorCode.NO_SUCH_TIME_TABLE.getMessage());
  }

  @Test
  void 사용_가능_강의실_생성_존재하지_않는_강의실_실패(){
    // Given
    Long lectureRoomId = 1666L;

    TimeTable timeTable = TimeTableCreateServiceTest.createTimeTable(timeTableJpaRepository, "테스트 시간표 1");

    UsableLectureRoomCreateDto.Request request = UsableLectureRoomCreateDto.Request.builder()
        .timeTableId(timeTable.getTimeTableId())
        .lectureRoomId(lectureRoomId)
        .build();

    // When

    // Then
    Assertions.assertThatThrownBy(() ->{
      usableLectureRoomCreateService.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(LectureRoomErrorCode.NO_SUCH_LECTURE_ROOM.getMessage());
  }

  private LectureRoom createLectureRoom(String building, Integer roomNumber){
    return lectureRoomJpaRepository.save(LectureRoom.builder()
        .building(building)
        .roomNumber(roomNumber)
        .capacity(50)
        .build());
  }

}
